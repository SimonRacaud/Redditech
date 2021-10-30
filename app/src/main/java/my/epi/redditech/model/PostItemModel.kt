package my.epi.redditech.model

import my.epi.redditech.model.api.*

class PostItemModel (
    val title: String = "Title undefined",
    val description: String = "Description undefined",
    val postUrl: String = "",
    val author: String = "",
    val preview: PreviewModel? = null,
    val created: Double = 0.0,
    val isVideo: Boolean = false,
    val thumbnail: String = "",
    val thumbnail_height: Int? = null,
    val thumbnail_width: Int? = null,
    val redirectUrl: String? = null,
    val subredditName: String = "",
    val mediaEmbed: SecureMediaEmbedModel? = null,
    val media: SecureMediaModel? = null,
    val domain: String? = null,
    val name: String = "",
    val postType: List<PostTypeModel>? = null,
    val postTypeTextColor: String = "dark",
    val postTypeBackgroundColor: String = ""
) {
    constructor (postModel: PostModel) : this(
        postModel.title,
        postModel.selftext,
        postModel.permalink,
        postModel.author,
        postModel.preview,
        postModel.created,
        postModel.is_video,
        postModel.thumbnail,
        postModel.thumbnail_height,
        postModel.thumbnail_width,
        postModel.url_overridden_by_dest,
        postModel.subreddit_name_prefixed,
        postModel.secure_media_embed,
        postModel.secure_media,
        postModel.domain,
        postModel.name,
        postModel.postType,
        postModel.postTypeTextColor,
        postModel.postTypeBackgroundColor
    )
}
