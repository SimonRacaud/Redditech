package com.epi.redditech

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity

internal class LoginWebViewClient : WebViewClient() {
    var token: String = ""

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        val uri = Uri.parse(url)

        println(uri.host + " " + uri.path)
        println("URL = $url")
        if (uri.host == "www.reddit.com" /*&& (uri.path == "/api/vi/authorize" || uri.path == "/login/")*/) {
            // This is my web site, so do not override; let my WebView load the page
            return false
        }
        val frag = uri.getEncodedFragment()
        val fragParse = TextUtils.split(frag, "&")
        for (value in fragParse) {
            val data = TextUtils.split(value, "=");
            if (data.size == 2 && data[0] == "access_token") {
                token = data[1]
            }
        }
        println("token $token") // We have the token
        return true
    }
}
