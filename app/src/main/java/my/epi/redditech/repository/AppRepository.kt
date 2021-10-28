package my.epi.redditech.repository

import my.epi.redditech.model.api.ListModel
import my.epi.redditech.model.api.PostModel
import my.epi.redditech.model.api.PrefModel
import my.epi.redditech.model.api.SubredditModel
import my.epi.redditech.network.ApiClient
import retrofit2.Response
import retrofit2.http.Path

class AppRepository {
    suspend fun getProfile() = ApiClient.apiReddit.getProfile()

    suspend fun getSettings() = ApiClient.apiReddit.getSettings()
    suspend fun setSettings(pref: PrefModel) = ApiClient.apiReddit.setSettings(pref)

    suspend fun getSubscribedSubreddit() = ApiClient.apiReddit.getSubscribeSubreddit()

    suspend fun getPostsFeed(sort : String) = ApiClient.apiReddit.getPostsFeed(sort)

    // Subreddit home
    suspend fun getSubredditInfo(subreddit : String) = ApiClient.apiReddit.getSubredditInfo(subreddit)
    suspend fun getSubredditPosts(subreddit: String, filter: String) = ApiClient.apiReddit.getSubredditPosts(subreddit, filter)

    // Search
    suspend fun getSubredditList(filter: String) = ApiClient.apiReddit.getSubredditList(filter)
    suspend fun getSubredditSearch(query: String) = ApiClient.apiReddit.getSubredditSearch(query)

    suspend fun subscribe(query: String, name: String) = ApiClient.apiReddit.subscribe(query, name)

    suspend fun getPostInformation(name : String) = ApiClient.apiReddit.getPostInformation(name)
}