package hu.vercibar.SchpincerNotifier

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception
import java.util.*

class CheckerService : Service() {

    lateinit var myHandler: Handler
    val searchText = "pizzasch"
    val CHANNEL_ID = "OPENING_AVAILABLE_CHANNEL"

    class AsyncTask(private val service: CheckerService, private val searchText: String) :
        TimerTask() {

        private val notificationId = 554346357

        override fun run() {
            try {
                val document: Document = Jsoup.connect("https://schpincer.sch.bme.hu/").get()
                val openingExists = document.select(".circles-table a").any {
                    it.attributes().get("href").contains(searchText)
                }
                if (openingExists) {

                    val alarmSound =
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    var notification = NotificationCompat.Builder(service, service.CHANNEL_ID)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.sandwich)
                        .setContentTitle("A ${searchText} körnek van aktív nyitása!")
                        .setContentText("Rendelj most, mielőtt minden hely elfogy!")
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setSound(alarmSound)
                        .setOnlyAlertOnce(true)
                        .build()

                    with(NotificationManagerCompat.from(service)) {
                        notify(notificationId, notification)
                        RingtoneManager.getRingtone(service, alarmSound).play()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            channel.setSound(
                alarmSound,
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val timer = Timer()
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()
        timer.scheduleAtFixedRate(AsyncTask(this, searchText), 0L, 60000L);
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        myHandler = Handler(Looper.getMainLooper())
        createNotificationChannel()
    }
}