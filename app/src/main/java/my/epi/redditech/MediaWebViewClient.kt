package my.epi.redditech

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient

internal class MediaWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
        return if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
            view.context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
            )
            true
        } else {
            false
        }
    }
}
