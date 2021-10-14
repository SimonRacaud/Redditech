package my.epi.redditech

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import my.epi.redditech.R.*
import my.epi.redditech.fragments.LoginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        createWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun createWebView() {
        // Inject login fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, LoginFragment())
        transaction.addToBackStack(null)
        transaction.commit()

    }
}