package my.epi.redditech.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import my.epi.redditech.R
import my.epi.redditech.model.PostItemModel
import my.epi.redditech.model.api.PostModel
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.utils.Utils
import my.epi.redditech.viewmodel.PostPageViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory
import my.epi.redditech.webViewClient.MediaWebViewClient

class PostPageActivity : AppCompatActivity() {
    private lateinit var loadingManager: LoadingManager
    private lateinit var viewModel: PostPageViewModel
    private lateinit var postInfo: PostModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_page)
        loadingManager = LoadingManager(supportFragmentManager)

        /// BACK BUTTON
        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }
        /// Get Parameters
        val intent = getIntent()
        val postName = intent.extras?.get("postName").toString()
        this.fetchData(postName)
    }


    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(PostPageViewModel::class.java)

    }

    private fun fetchData(postName: String) {
        this.setupViewModel()
        loadingManager.startLoading()
        viewModel.post.observe(this, {
            this.buildPage(it.data.children[0].data)
        })
        viewModel.errorMessage.observe(this, {
            //TODO use it
            loadingManager.stopLoading()
        })
        viewModel.loading.observe(this, {
            if (it) {
                //TODO: it show progress bar (loading...)
            } else {
                //TODO: mask progress
                loadingManager.stopLoading()
            }
        })
        viewModel.getPostPage(postName)
    }

    private fun buildPage(postInfo: PostModel) {
        buildInfo(postInfo)
        buildContent(postInfo)
    }

    private fun buildInfo(postInfo : PostModel) {
        var subredditName = findViewById<TextView>(R.id.subreddit_title)
        var postDate = findViewById<TextView>(R.id.post_utc)
        var author = findViewById<TextView>(R.id.subreddit_desc)
        var subscriberNumber = findViewById<TextView>(R.id.header_nb_subscribers)
        var buttonSubscribe = findViewById<Button>(R.id.subscribe_button)
        val headerImg = findViewById<ImageView>(R.id.subredit_img_header)
        val iconImg = findViewById<ImageView>(R.id.community_icon)

        /*var bannerUrl = //banner_background_image
        bannerUrl = bannerUrl?.replace("&amp;", "&")
        Glide.with(this).load(bannerUrl).into(headerImg)


        var comunityIconUrl = //data.community_icon
        comunityIconUrl = comunityIconUrl?.replace("&amp;", "&")
        Glide.with(this).load(comunityIconUrl).into(iconImg)*/

        subredditName.text = postInfo.subreddit_name_prefixed
        postDate.text = Utils.formatDate(postInfo.created_utc.toLong())
        author.text = "Posted by " + postInfo.author
        subscriberNumber.text = Utils.getFormatNumber(postInfo.subreddit_subscribers)
        buttonSubscribe.visibility = View.INVISIBLE
        buttonSubscribe.isEnabled = false
    }

    private fun buildContent(postInfo: PostModel) {
        val current = PostItemModel(postInfo)
        var postTitle = findViewById<TextView>(R.id.post_title)
        var postContent = findViewById<TextView>(R.id.post_content)
        var picture = findViewById<ImageView>(R.id.picture)
        var media = findViewById<WebView>(R.id.media_view)
        var redirectButton = findViewById<Button>(R.id.redirect_button)
        var media_view_container = findViewById<LinearLayout>(R.id.media_view_container)

        postTitle.text = postInfo.title
        postContent.text = postInfo.selftext
        postContent.movementMethod = ScrollingMovementMethod()

        /// BUTTON
        if (!current.redirectUrl.isNullOrEmpty()) {
            val url = Uri.parse(current.redirectUrl)
            if (url.host.isNullOrEmpty() == false) {
                redirectButton.text = "Go to ${url.host}"
            }
            redirectButton.setOnClickListener { onClickRedirect(current.redirectUrl) }
            redirectButton.visibility = View.VISIBLE
        }
        if (current.preview != null && current.preview.reddit_video_preview != null
            && current.preview.reddit_video_preview.fallback_url != "") {
            /// VIDEO PREVIEW
            this.loadMedia(
                current.preview.reddit_video_preview.fallback_url,
                media,
                media_view_container)
            println("SHOW PREVIEW VIDEO : ${current.preview.reddit_video_preview.fallback_url}")
        } else if (current.mediaEmbed != null && !current.mediaEmbed.media_domain_url.isNullOrEmpty()) {
            /// EMBED MEDIA
            this.loadMedia(current.mediaEmbed.media_domain_url, media, media_view_container)
        } else if (current.media != null && current.media.reddit_video != null
            && current.media.reddit_video.fallback_url != "") {
            /// MEDIA
            this.loadMedia(current.media.reddit_video.fallback_url, media, media_view_container)
        }
        // PICTURE
        if (!current.redirectUrl.isNullOrEmpty()
            && media_view_container.visibility == View.GONE
        ) {
            val extension = current.redirectUrl.substringAfterLast('.')

            if (extension == "png" || extension == "jpg" || extension == "gif") {
                Glide.with(this).load(Uri.parse(current.redirectUrl)).into(picture)
                picture.visibility = View.VISIBLE
            }
        }
    }

    private fun loadMedia(url: String, media: WebView, container: LinearLayout) {
        media.loadUrl(url)
        media.settings?.allowContentAccess = true
        media.settings?.allowFileAccess = true
        media.settings?.javaScriptEnabled = true
        media.webViewClient = MediaWebViewClient()
        container.visibility = View.VISIBLE
    }

    private fun onClickRedirect(url: String) {
        val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        this.startActivity(browseIntent)
    }

}