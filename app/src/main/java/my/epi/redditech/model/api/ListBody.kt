package my.epi.redditech.model.api

data class ListBody<T> (
    val after: String?,             // Next page ('name' row)
    val before: String?,            // Previous page ('name' row)
    val children: List<ItemModel<T>>,  // List items
    val dist: Int,                  // List size
)