package my.epi.redditech.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import my.epi.redditech.webViewClient.MediaWebViewClient
import my.epi.redditech.R
import my.epi.redditech.activity.PostPageActivity
import my.epi.redditech.activity.SubredditActivity
import my.epi.redditech.model.PostItemModel

/**
 * Post List Adapter
 */
class PostListAdapter(
    private val context: Context?,
    private val itemList: MutableList<PostItemModel>,
    private val layoutId: Int
) : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    // Post Item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById<ImageView>(R.id.imageViewPostIcon)
        val subredditName: TextView = view.findViewById<TextView>(R.id.subreddit_name)
        val content: TextView = view.findViewById<TextView>(R.id.content)
        val author: TextView = view.findViewById<TextView>(R.id.author)
        val redirectButton: Button = view.findViewById<Button>(R.id.redirect_button)
        val media: WebView = view.findViewById<WebView>(R.id.media_view)
        val media_view_container: LinearLayout = view.findViewById(R.id.media_view_container)
        val picture: ImageView = view.findViewById<ImageView>(R.id.picture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostListAdapter.ViewHolder, position: Int) {
        val current = itemList[position]

        /// INIT
        holder.media_view_container.visibility = View.GONE
        holder.redirectButton.visibility = View.GONE
        holder.picture.visibility = View.GONE
        /// HEADER
        if (context != null && !current.thumbnail.isEmpty()) {
            Glide.with(context).load(Uri.parse(current.thumbnail)).into(holder.icon)
        }
        holder.itemView.setOnClickListener { onClickEventView(current.subredditName, current.name) }
        holder.subredditName.text = current.subredditName
        holder.subredditName.setOnClickListener { onClickSubreddit(current.subredditName) }
        holder.author.text = current.author
        holder.content.text = current.title
        /// BUTTON
        if (!current.redirectUrl.isNullOrEmpty()) {
            val url = Uri.parse(current.redirectUrl)
            if (url.host.isNullOrEmpty() == false) {
                holder.redirectButton.text = "Go to ${url.host}"
            }
            holder.redirectButton.setOnClickListener { onClickRedirect(current.redirectUrl) }
            holder.redirectButton.visibility = View.VISIBLE
        }
        if (current.preview != null && current.preview.reddit_video_preview != null
            && current.preview.reddit_video_preview.fallback_url != "") {
            /// VIDEO PREVIEW
            this.loadMedia(
                current.preview.reddit_video_preview.fallback_url,
                holder.media,
                holder.media_view_container)
        } else if (current.mediaEmbed != null && !current.mediaEmbed.media_domain_url.isNullOrEmpty()) {
            /// EMBED MEDIA
            this.loadMedia(current.mediaEmbed.media_domain_url, holder.media, holder.media_view_container)
        } else if (current.media != null && current.media.reddit_video != null
            && current.media.reddit_video.fallback_url != "") {
            /// MEDIA
            this.loadMedia(current.media.reddit_video.fallback_url, holder.media, holder.media_view_container)
        }
        // PICTURE
        if (context != null && !current.redirectUrl.isNullOrEmpty()
            && holder.media_view_container.visibility == View.GONE
        ) {
            val extension = current.redirectUrl.substringAfterLast('.')

            if (extension == "png" || extension == "jpg" || extension == "gif") {
                Glide.with(context).load(Uri.parse(current.redirectUrl)).into(holder.picture)
                holder.picture.visibility = View.VISIBLE
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

    private fun onClickEventView(pageName: String, postName : String) {
        val intent = Intent(context, PostPageActivity::class.java)
        intent.putExtra("subredditName", pageName)
        intent.putExtra("postName", postName)
        context?.startActivity(intent)
    }

    private fun onClickRedirect(url: String) {
        val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context?.startActivity(browseIntent)
    }

    private fun onClickSubreddit(pageName: String) {
        val intent = Intent(context, SubredditActivity::class.java)
        intent.putExtra("subredditName", pageName)
        context?.startActivity(intent)
    }

    override fun getItemCount(): Int = itemList.size

    fun append(list: List<PostItemModel>) {
        val oldSize = itemList.size
        itemList.addAll(list)
        this.notifyItemRangeInserted(oldSize, list.size)
    }

    fun clear() {
        val size = itemList.size

        itemList.clear()
        this.notifyItemRangeRemoved(0, size)
    }
}
