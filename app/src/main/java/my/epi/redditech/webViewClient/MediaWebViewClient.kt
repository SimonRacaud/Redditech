package my.epi.redditech.webViewClient

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Post item media - web view client
 */
internal class MediaWebViewClient : WebViewClient() {
    /**
     * What is that ??? - Loading the content maybe ?
     */
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

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)

        // Hide on error
        //view?.visibility = View.GONE
        println("ERROR LOADING MEDIA CONTENT ${error?.description.toString()}")
    }
}
