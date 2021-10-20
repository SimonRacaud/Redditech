package my.epi.redditech.model.api

data class PrefModel(
    val country_code: String,
    val lang: String,

    val email_unsubscribe_all: Boolean,
    val enable_followers: Boolean,
    val label_nsfw: Boolean,
    val over_18: Boolean,
    val search_include_over_18: Boolean,
    val no_profanity: Boolean,
    val nightmode: Boolean,
    val video_autoplay: Boolean,
    val send_welcome_messages: Boolean,
    val show_snoovatar: Boolean,
    val show_trending: Boolean,
    val num_comments: Int,
    val third_party_data_personalized_ads: Boolean,
    val third_party_personalized_ads: Boolean,
    val third_party_site_data_personalized_ads: Boolean,
    val third_party_site_data_personalized_content: Boolean,
)