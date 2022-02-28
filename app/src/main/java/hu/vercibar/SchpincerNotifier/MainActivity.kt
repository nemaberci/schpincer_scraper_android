package hu.vercibar.SchpincerNotifier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import hu.vercibar.SchpincerNotifier.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setSupportActionBar(binding.toolbar)

        Intent(this, CheckerService::class.java).also {
            intent -> startService(intent)
        }
        registerReceiver(broadcastReceiver, IntentFilter("refreshDate"))
    }

    private val broadcastReceiver = object: BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent?) {
            findViewById<TextView>(R.id.lastChecked).text = LocalDateTime.now().format(
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Intent(this, CheckerService::class.java).also {
            intent -> stopService(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.settings_menu, menu)

        menu?.getItem(0)?.setOnMenuItemClickListener {
            var intent = Intent(
                this,
                SettingsActivity::class.java
            )
            startActivity(intent)
            true
        }
        return true
    }
}