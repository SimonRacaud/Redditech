package my.epi.redditech.model.api

data class PrefModel(
    var country_code: String,
    var lang: String,

    var email_unsubscribe_all: Boolean,
    var enable_followers: Boolean,          // change
    var label_nsfw: Boolean,
    var over_18: Boolean,                   // change
    var search_include_over_18: Boolean,    // change
    var no_profanity: Boolean,
    var nightmode: Boolean,
    var video_autoplay: Boolean,            // change
    var send_welcome_messages: Boolean,
    var show_snoovatar: Boolean,
    var show_trending: Boolean,
    var num_comments: Int,
    var third_party_data_personalized_ads: Boolean,         // change
    var third_party_personalized_ads: Boolean,              // change
    var third_party_site_data_personalized_ads: Boolean,    // change
    var third_party_site_data_personalized_content: Boolean, // change
)