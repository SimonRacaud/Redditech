package my.epi.redditech.model.api

import com.google.gson.annotations.SerializedName

data class PostModel(
    val author: String, // author username
    val author_fullname: String, // author unique id ex: "t2_4zlgiopm"

    val title: String,

    @SerializedName("link_flair_richtext")
    val postType: List<PostTypeModel>?,
    @SerializedName("link_flair_text_color")
    val postTypeTextColor: String,
    @SerializedName("link_flair_background_color")
    val postTypeBackgroundColor: String,

    val id: String,
    val name: String, // unique id (listing)

    val permalink: String, // url (begin by /r/...)
    val url: String, // full http url

    val preview: PreviewModel?, // post content (images, videos)

    val selftext: String, // text content
    val selftext_html: String, // text content html

    val subreddit: String, // type : "Advertisesub" | <subreddit name>
    val subreddit_id: String,
    val subreddit_name_prefixed: String, // parent name
    val subreddit_subscribers: Int,

    val thumbnail: String, // "self" | "default" | "nsfw" | <url>
    val thumbnail_height: Int?,
    val thumbnail_width: Int?,

    val url_overridden_by_dest: String?, // redirect url
    val domain: String?,

    val view_count: Int?,
    val visited: Boolean,
    val clicked: Boolean,
    val created: Double,
    val created_utc: Double,
    val hidden: Boolean,
    val hide_score: Boolean,
    val locked: Boolean,
    val media_only: Boolean,
    val is_video: Boolean,
    val num_comments: Int,
    val score: Int,
    val spoiler: Boolean,

    val secure_media_embed: SecureMediaEmbedModel?,
    val secure_media: SecureMediaModel?,
)