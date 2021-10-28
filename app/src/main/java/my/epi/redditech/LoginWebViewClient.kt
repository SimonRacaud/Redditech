package my.epi.redditech

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import my.epi.redditech.activity.AuthActivity
import my.epi.redditech.activity.StartActivity

internal class LoginWebViewClient(val primaryUrl: String, val context: AuthActivity) : WebViewClient() {
    private var code: String = ""

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        val uri = Uri.parse(url)

        println(uri.host + " " + uri.path)
        println("URL = $url")

        if (uri.host == "www.reddit.com" && (uri.path!!.startsWith("/api", false)
                    || uri.path!!.startsWith("/login", false))) {
            return false // This is my web site, so do not override; let my WebView load the page
        } else if (uri.host == "www.reddit.com" && uri?.path == "/") {
            view?.loadUrl(primaryUrl) // go back to authorization page after login
        }
        // Extract auth token
        val codeParams = uri.getQueryParameters("code") ?: null
        if (codeParams != null && codeParams[0] != null) {
            code = codeParams[0]
        }
        if (code != "") {
            println("Log: token $code") // Debug : We have the token
            context.applyLogin(code)
        } else {
            println("Log: token not found")
        }
        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        val uri = Uri.parse(url)

        if (uri.host == "www.reddit.com" && uri.path!!.startsWith("/api/v1/authorize", false)) {
            view?.loadUrl(
                """javascript:(function f() {
                        document.getElementById("header").style.display = "none";
                        document.getElementsByClassName("footer-parent")[0].style.display = "none";
                        document.getElementsByClassName("oauth2-authorize")[0].style.paddingLeft = "0px";
                        document.getElementsByClassName("oauth2-authorize")[0].style.background = "none";
                        document.getElementsByClassName("icon")[0].style.display = "none";
                        var divNode = document.createElement("div");
                        divNode.innerHTML = "<br><style>
                            .access {
                              width: 90vw !important;
                              float: none !important;;
                            }
                            .oauth2-authorize h1 {
                              width: 90vw;
                            }
                            </style>";
                        document.body.appendChild(divNode);
                    })()"""
            )
        }
    }
}
