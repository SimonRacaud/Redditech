package com.epi.redditech

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.MenuItem
import android.view.Menu
import android.webkit.WebView
import com.epi.redditech.databinding.ActivityMainBinding
import android.net.http.SslError

import android.webkit.SslErrorHandler
import android.webkit.WebViewClient
import android.R.string
import com.epi.redditech.R

import java.util.Dictionary

fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, er: SslError?) {
    handler.proceed()
}

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var authToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // set app design (layout)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val myWebView = findViewById<WebView>(R.id.webview);

        //headers.Add("Authorization", "Bearer");
        myWebView.settings.allowContentAccess = true;
        myWebView.settings.allowFileAccess = true;
        myWebView.settings.javaScriptEnabled = true;
        myWebView.webViewClient = LoginWebViewClient();
        myWebView.loadUrl("https://www.reddit.com/api/v1/authorize?client_id=e14xOSvlU0BhmAuhho8z3Q&response_type=token&state=foobarjaaj&redirect_uri=https://localhost:8080/callback&scope=identity%20edit");
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}