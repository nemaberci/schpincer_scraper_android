package hu.vercibar.SchpincerNotifier

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import hu.vercibar.SchpincerNotifier.databinding.ActivityMainBinding


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