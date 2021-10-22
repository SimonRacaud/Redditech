package my.epi.redditech.model.api

data class UserModel(
    val name: String,           // username
    val id: String,             // unique id
    val oauth_client_id: String = "", // useful ?
    val description: String = "",
    val subreddit: SubredditModel,

    val icon_img: String = "",       // icon
    val snoovatar_img: String = "",  // icon url
    val snoovatar_size: List<Int> = listOf(0), // icon size

    val num_friends: Int = 0,
    val total_karma: Int = 0,

    val pref_autoplay: Boolean = false,
    val pref_nightmode: Boolean = false,
    val pref_show_snoovatar: Boolean = false,
    val pref_show_trending: Boolean = false,
    val pref_video_autoplay: Boolean = false,

    val created: Double = 0.0,
    val created_utc: Double = 0.0,
)