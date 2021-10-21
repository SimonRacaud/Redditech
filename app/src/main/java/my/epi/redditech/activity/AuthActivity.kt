package my.epi.redditech.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import my.epi.redditech.LoginWebViewClient
import my.epi.redditech.R
import my.epi.redditech.network.ApiClient

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        this.createWebView()
    }

    private fun createWebView() {
        val myWebView = findViewById<WebView>(R.id.webview)
        val url = this.getString(R.string.auth_url)
        myWebView?.loadUrl(url)
        myWebView?.settings?.allowContentAccess = true
        myWebView?.settings?.allowFileAccess = true
        myWebView?.settings?.javaScriptEnabled = true
        myWebView?.webViewClient = LoginWebViewClient(url, this)
    }

    fun applyLogin(token: String) {
        val intent = Intent(this, HomeActivity::class.java)
        finish()
        startActivity(intent)

        /// TODO : we have to token. Now, what do we do with it ?
        /// Save token in preferences
        ApiClient.token = token;
    }
}