package my.epi.redditech.model.api

/**
 * List item
 */
data class ItemModel<T>(
    val `data`: T,
    val kind: String
)