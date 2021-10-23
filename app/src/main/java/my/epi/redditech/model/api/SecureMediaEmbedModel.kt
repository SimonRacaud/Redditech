package my.epi.redditech.model.api

/**
 * For video integration
 */
data class SecureMediaEmbedModel (
    val width: Int,
    val height: Int,
    val media_domain_url: String?,
)