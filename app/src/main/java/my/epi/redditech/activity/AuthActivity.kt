package my.epi.redditech.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import androidx.lifecycle.ViewModelProvider
import my.epi.redditech.LoginWebViewClient
import my.epi.redditech.R
import my.epi.redditech.network.ApiClient
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.viewmodel.AuthViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory

class AuthActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

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

    fun applyLogin(code: String) {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        viewModel.oauthToken.observe(this, {
            ApiClient.token = it.accessToken
            val intent = Intent(this, HomeActivity::class.java)
            finish()
            startActivity(intent)
        })
        viewModel.errorMessage.observe(this, {
            //TODO use it
        })
        viewModel.loading.observe(this, {
            if (it) {
                //TODO: it show progress bar (loading...)
            } else {
                //TODO: mask progress
            }
        })
        viewModel.getToken(code)
    }
}