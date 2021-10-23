package my.epi.redditech.network

import my.epi.redditech.model.api.*
import retrofit2.Response
import retrofit2.http.*

interface ApiReddit {

    @GET("api/v1/me")
    suspend fun getProfile(): Response<UserModel>

    @GET("/api/v1/me/prefs")
    suspend fun getSettings(): Response<PrefModel>

    @PATCH("/api/v1/me/prefs")
    suspend fun setSettings(@Body pref: PrefModel): Response<PrefModel>

    @GET("/subreddits/mine/subscriber")
    suspend fun getSubscribeSubreddit(): Response<ListModel<SubredditModel>>

    @GET("/{sort}")
    suspend fun getPostsFeed(@Path("sort") sort : String): Response<ListModel<PostModel>>

    @GET("/r/{subreddit}/about")
    suspend fun getSubredditInfo(@Path("subreddit") subreddit : String): Response<ItemModel<SubredditModel>>

    @GET("subreddits/{filter}")
    suspend fun getSubredditList(@Path("filter") filter: String): Response<ListModel<SubredditModel>>

    @GET("api/subreddit_autocomplete_v2")
    suspend fun getSubredditSearch(@Query("query") query: String,
                                   @Query("include_profiles") include_profiles : String = "false"): Response<ListModel<SubredditModel>>
    
}