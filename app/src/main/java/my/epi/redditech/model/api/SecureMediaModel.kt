package my.epi.redditech.model.api

data class SecureMediaModel (
    val reddit_video: RedditVideoModel?,
)

data class RedditVideoModel (
    val fallback_url: String, // html player
    val height: Int,
    val width: Int,
    val dash_url: String, // file link
    val is_gif: Boolean,
    val transcoding_status: String,
)