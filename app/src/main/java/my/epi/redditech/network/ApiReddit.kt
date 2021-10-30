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
    suspend fun getSubscribeSubreddit(@Query("after") after: String): Response<ListModel<SubredditModel>>

    @GET("/{sort}")
    suspend fun getPostsFeed(@Path("sort") sort : String, @Query("after") after: String): Response<ListModel<PostModel>>

    @GET("/r/{subreddit}/about")
    suspend fun getSubredditInfo(@Path("subreddit") subreddit : String): Response<ItemModel<SubredditModel>>

    @GET("subreddits/{filter}")
    suspend fun getSubredditList(@Path("filter") filter: String): Response<ListModel<SubredditModel>>

    @GET("subreddits/search")
    suspend fun getSubredditSearch(@Query("q") query: String): Response<ListModel<SubredditModel>>

    @GET("{subreddit}/{filter}")
    suspend fun getSubredditPosts(@Path("subreddit") subreddit: String, @Path("filter") filter: String,
                                  @Query("after") after: String, @Query("limit") limit: String = "10"): Response<ListModel<PostModel>>

    @GET("api/subreddit_autocomplete_v2")
    suspend fun getSubredditSearch(@Query("query") query: String,
                                   @Query("include_profiles") include_profiles : String = "false"): Response<ListModel<SubredditModel>>

    @POST("/api/subscribe")
    suspend fun subscribe(@Query("action") query : String, @Query("sr_name") name : String): Response<Void>

    @GET("/by_id/{name}")
    suspend fun getPostInformation(@Path("name") name : String): Response<ListModel<PostModel>>
}