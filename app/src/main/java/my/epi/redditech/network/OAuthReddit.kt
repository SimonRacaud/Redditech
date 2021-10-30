package my.epi.redditech.network

import my.epi.redditech.model.api.OAuthModel
import retrofit2.Response
import retrofit2.http.*

/**
 * List of the API's endpoints - auth only
 */
interface OAuthReddit {

    @FormUrlEncoded
    @POST("/api/v1/access_token")
    suspend fun getToken(@Field("code") code : String,
                         @Field("redirect_uri") redirectUri : String = "https://localhost:8080/callback",
                         @Field("grant_type") grantType : String = "authorization_code"): Response<OAuthModel>
}