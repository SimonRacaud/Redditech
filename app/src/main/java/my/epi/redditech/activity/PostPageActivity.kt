package my.epi.redditech.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
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
import my.epi.redditech.model.api.SubredditModel
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.utils.ErrorMessage
import my.epi.redditech.utils.LoadingManager
import my.epi.redditech.utils.Utils
import my.epi.redditech.viewmodel.PostPageViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory
import my.epi.redditech.webViewClient.MediaWebViewClient

/**
 * The the content of a specific post
 */
class PostPageActivity : AppCompatActivity() {
    private lateinit var loadingManager: LoadingManager
    private lateinit var viewModel: PostPageViewModel
    private lateinit var postInfo: PostModel
    private lateinit var subredditInfo: SubredditModel

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
        val subredditName = intent.extras?.get("subredditName").toString()
        this.fetchData(postName, subredditName.drop(2))
    }

    /**
     * Network initialisation
     */
    private fun fetchData(postName: String, subredditName: String) {
        this.setupViewModel()
        loadingManager.startLoading()
        this.getPostContent(postName)
        this.getSubredditInfo(subredditName)
    }

    /**
     * Setup the view model (network)
     */
    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(PostPageViewModel::class.java)
    }

    /**
     * Getting the post content though the network
     */
    private fun getPostContent(postName: String) {
        viewModel.post.observe(this, {
            postInfo = it.data.children[0].data
            this.buildPage(postInfo)
        })
        viewModel.errorMessage.observe(this, {
            loadingManager.stopLoading()
            ErrorMessage.show(this, it)
        })
        viewModel.loading.observe(this, {
            if (!it) {
                loadingManager.stopLoading()
            }
        })
        viewModel.getPostPage(postName)
    }

    /**
     * Get the information of the post's subreddit (network)
     */
    private fun getSubredditInfo(subredditName: String) {
        viewModel.subredditInfo.observe(this, {
            subredditInfo = it.data
            this.buildHeader(subredditInfo)
        })
        viewModel.errorMessage.observe(this, {
            ErrorMessage.show(this, it)
        })
        viewModel.getInfoSubreddit(subredditName)
    }

    /**
     * Fill the view - page header - load images
     */
    private fun buildHeader(subredditInfo: SubredditModel) {
        val headerImg = findViewById<ImageView>(R.id.subredit_img_header)
        val iconImg = findViewById<ImageView>(R.id.community_icon)

        var bannerUrl = subredditInfo.banner_background_image
        if (bannerUrl.isNullOrEmpty()) {
            var background = findViewById<TextView>(R.id.background)
            background.visibility = View.VISIBLE
            headerImg.visibility = View.INVISIBLE
            if (!subredditInfo.key_color.isNullOrEmpty()) {
                background.background = ColorDrawable(Color.parseColor(subredditInfo.key_color))
            } else if (!subredditInfo.banner_background_color.isNullOrEmpty()) {
                background.background = ColorDrawable(Color.parseColor(subredditInfo.banner_background_color))
            } else if (!subredditInfo.primary_color.isNullOrEmpty()) {
                background.background = ColorDrawable(Color.parseColor(subredditInfo.primary_color))
            }

        }
        bannerUrl = bannerUrl?.replace("&amp;", "&")
        Glide.with(this).load(bannerUrl).into(headerImg)

        var comunityIconUrl = ""
        if (subredditInfo.community_icon.toString().isNotEmpty()) {
            comunityIconUrl = subredditInfo.community_icon.toString()
        } else if (subredditInfo.icon_img.isNotEmpty()) {
            comunityIconUrl = subredditInfo.icon_img
        }
        if (comunityIconUrl.isNotEmpty()) {
            comunityIconUrl = comunityIconUrl?.replace("&amp;", "&")
            Glide.with(this).load(comunityIconUrl).into(iconImg)
        }
    }

    /**
     * Fill the view - page meta and content
     */
    private fun buildPage(postInfo: PostModel) {
        buildInfo(postInfo)
        buildContent(postInfo)
    }

    /**
     * Fill the view - page meta data
     */
    private fun buildInfo(postInfo : PostModel) {
        var subredditName = findViewById<TextView>(R.id.subreddit_title)
        var postDate = findViewById<TextView>(R.id.post_utc)
        var author = findViewById<TextView>(R.id.subreddit_desc)
        var subscriberNumber = findViewById<TextView>(R.id.header_nb_subscribers)
        var buttonSubscribe = findViewById<Button>(R.id.subscribe_button)
        var postType = findViewById<TextView>(R.id.post_type)

        subredditName.text = postInfo.subreddit_name_prefixed
        postDate.text = Utils.formatDate(postInfo.created_utc.toLong())
        author.text = "Posted by " + postInfo.author
        subscriberNumber.text = Utils.getFormatNumber(postInfo.subreddit_subscribers)
        buttonSubscribe.visibility = View.INVISIBLE
        buttonSubscribe.isEnabled = false
        if (postInfo.postType != null && postInfo.postType.size != 0) {
            postType.visibility = View.VISIBLE
            postType.text = postInfo.postType[0].label
            if (!postInfo.postTypeBackgroundColor.isNullOrEmpty())
                postType.background = ColorDrawable(Color.parseColor(postInfo.postTypeBackgroundColor))
            if (!postInfo.postTypeTextColor.isNullOrEmpty()) {
                if (postInfo.postTypeTextColor == "dark") {
                    postType.setTextColor(Color.BLACK)
                } else if (postInfo.postTypeTextColor == "light") {
                    postType.setTextColor(Color.WHITE)
                }
            }
        }
    }

    /**
     * Fill the view - page content
     */
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

    /**
     * Load media through a webview (videos)
     */
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