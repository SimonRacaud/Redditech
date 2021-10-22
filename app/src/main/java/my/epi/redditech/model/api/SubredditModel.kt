package my.epi.redditech.model.api

data class SubredditModel (
    val banner_img: String,
    val banner_size: List<Int>?,
    val banner_background_image: String?,
    val banner_background_color: String?,
    val is_default_banner: Boolean,
    val mobile_banner_image: String?,

    val header_img: String?,     // header url : very small header img
    val header_size: List<Int>?, // header
    val header_title: String?,

    val primary_color: String,
    val key_color: String,

    val is_default_icon: Boolean,
    val icon_color: String,
    val icon_img: String,           // icon url (may be empty)
    val icon_size: List<Int>,       // icon size
    var community_icon: String?,    // icon url 2 (may be empty)

    val accept_followers: Boolean,  // allow subscribe ?
    val subscribers: Int,           // nb subscribers
    val user_is_subscriber: Boolean,// subscribed
    val url: String,    // page url
    val name: String,   // page unique id (listing)
    val id: String,

    val public_description: String,     // description (short format)
    val description: String,            // description (long format)
    val description_html: String?,
    val public_description_html: String?,
    val display_name: String,           // title
    val title: String,                  // full title
    val display_name_prefixed: String,  // sub title

    val created: Float?,
    val created_utc: Float?
)