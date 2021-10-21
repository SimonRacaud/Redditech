package my.epi.redditech.model.api

data class ListModel (
    val `data`: ListBody,
    val kind: String // == "Listing"
)