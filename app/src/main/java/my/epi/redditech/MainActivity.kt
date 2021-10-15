package my.epi.redditech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import my.epi.redditech.R.*
import my.epi.redditech.activity.StartActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }
}