package my.epi.redditech.model.api

data class ListBody (
    val after: String?,             // Next page ('name' row)
    val before: String?,            // Previous page ('name' row)
    val children: List<ItemModel>,  // List items
    val dist: Int,                  // List size
)