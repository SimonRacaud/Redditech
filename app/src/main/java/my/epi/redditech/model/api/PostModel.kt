package my.epi.redditech.model.api

data class PostModel(
    val author: String, // author username
    val author_fullname: String, // author unique id ex: "t2_4zlgiopm"

    val title: String,

    val id: String,
    val name: String, // unique id (listing)

    val permalink: String, // url (begin by /r/...)
    val url: String, // full http url

    val preview: PreviewModel?, // post content (images, videos)

    val selftext: String, // text content
    val selftext_html: String, // text content html

    val subreddit: String, // type : "Advertisesub" | "subreddit"
    val subreddit_id: String,
    val subreddit_name_prefixed: String, // parent name
    val subreddit_subscribers: Int,

    val thumbnail: String, // "self" | "default" | "nsfw"
    val thumbnail_height: Int?,
    val thumbnail_width: Int?,

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
)