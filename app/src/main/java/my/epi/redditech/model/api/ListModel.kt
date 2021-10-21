package my.epi.redditech.model.api

data class ListModel<T> (
    val `data`: ListBody<T>,
    val kind: String // == "Listing"
)