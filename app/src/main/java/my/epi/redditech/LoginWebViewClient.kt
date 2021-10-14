package my.epi.redditech

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.webkit.WebView
import android.webkit.WebViewClient

internal class LoginWebViewClient(val primaryUrl: String, val context: MainActivity) : WebViewClient() {
    private var token: String = ""

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        val uri = Uri.parse(url)

        println(uri.host + " " + uri.path)
        println("URL = $url")
        if (uri.host == "www.reddit.com" && uri.path != null
            && (uri.path!!.startsWith("/api", false)
                    || uri.path!!.startsWith("/login", false))) {
            // This is my web site, so do not override; let my WebView load the page
            return false
        } else if (uri.host == "www.reddit.com" && uri?.path == "/") {
            view?.loadUrl(primaryUrl) // go back to authorization page after login
        }
        // Extract auth token
        val frag = uri.encodedFragment
        if (frag != null) {
            val fragParse = TextUtils.split(frag, "&")
            for (value in fragParse) {
                val data = TextUtils.split(value, "=");
                if (data.size == 2 && data[0] == "access_token") {
                    token = data[1]
                }
            }
        }
        if (token != "") {
            println("token $token") // Debug : We have the token
        } else {
            println("token not found")
        }
        /// Save token in preferences
        val preferences = context.getPreferences(Context.MODE_PRIVATE)
        with (preferences.edit()) {
            putString("AUTH_TOKEN", "$token")
            apply()
        }
        return true
    }
}
