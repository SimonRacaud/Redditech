package my.epi.redditech.model.api

import com.google.gson.annotations.SerializedName

data class PostTypeModel(
    @SerializedName("e")
    val type: String, //post type
    @SerializedName("t")
    val label: String //post label
)
