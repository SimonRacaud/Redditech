package my.epi.redditech.model.api

data class UserModel(
    val name: String,           // username
    val id: String,             // unique id
    val oauth_client_id: String, // useful ?
    val description: String,

    val icon_img: String,       // icon
    val snoovatar_img: String,  // icon url
    val snoovatar_size: List<Int>, // icon size

    val num_friends: Int,
    val total_karma: Int,

    val pref_autoplay: Boolean,
    val pref_nightmode: Boolean,
    val pref_show_snoovatar: Boolean,
    val pref_show_trending: Boolean,
    val pref_video_autoplay: Boolean,

    val created: Double,
    val created_utc: Double,
)