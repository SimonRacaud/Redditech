package my.epi.redditech.model.api

import com.google.gson.annotations.SerializedName

data class OAuthModel(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    val scope: String
)
