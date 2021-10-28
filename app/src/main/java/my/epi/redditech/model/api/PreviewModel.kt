package my.epi.redditech.model.api

data class PreviewModel(
    val enabled: Boolean,
    val images: List<ImageModel>,
    val reddit_video_preview: VideoPreview?,
)