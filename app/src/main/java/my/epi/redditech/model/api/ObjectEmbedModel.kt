package my.epi.redditech.model.api

data class ObjectEmbedModel(
    val title: String,
    val html: String,
    val thumbnail_width: Int,
    val height: Int,
    val width: Int,
    val thumbnail_url: String,
)