package my.epi.redditech.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import my.epi.redditech.R

class PostPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_page)

        /// BACK BUTTON
        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }
    }
}