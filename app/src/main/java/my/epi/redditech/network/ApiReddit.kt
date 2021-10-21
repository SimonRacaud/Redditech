package my.epi.redditech.network

import my.epi.redditech.model.api.*
import retrofit2.Response
import retrofit2.http.*

interface ApiReddit {

    @GET("api/v1/me")
    suspend fun getProfile(): Response<UserModel>

    @GET("/api/v1/me/prefs")
    suspend fun getSettings(): Response<PrefModel>

    @GET("/subreddits/mine/subscriber")
    suspend fun getSubscribeSubreddit(): Response<ListModel<SubredditModel>>
}