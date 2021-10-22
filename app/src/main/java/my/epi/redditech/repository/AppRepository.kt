package my.epi.redditech.repository

import my.epi.redditech.model.api.PrefModel
import my.epi.redditech.network.ApiClient

class AppRepository {
    suspend fun getProfile() = ApiClient.apiReddit.getProfile()

    suspend fun getSettings() = ApiClient.apiReddit.getSettings()
    suspend fun setSettings(pref: PrefModel) = ApiClient.apiReddit.setSettings(pref)

    suspend fun getSubscribedSubreddit() = ApiClient.apiReddit.getSubscribeSubreddit()

    suspend fun getPostsFeed(sort : String) = ApiClient.apiReddit.getPostsFeed(sort)
}