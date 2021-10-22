package my.epi.redditech.repository

import my.epi.redditech.network.ApiClient

class AppRepository {
    suspend fun getProfile() = ApiClient.apiReddit.getProfile()

    suspend fun getSettings() = ApiClient.apiReddit.getSettings()

    suspend fun getSubscribedSubreddit() = ApiClient.apiReddit.getSubscribeSubreddit()

    suspend fun getPostsFeed(sort : String) = ApiClient.apiReddit.getPostsFeed(sort)

    suspend fun getSubredditInfo(subreddit : String) = ApiClient.apiReddit.getSubredditInfo(subreddit)
}