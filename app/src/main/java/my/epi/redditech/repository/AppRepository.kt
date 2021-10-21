package my.epi.redditech.repository

import my.epi.redditech.network.ApiClient

class AppRepository {
    suspend fun getProfile() = ApiClient.apiReddit.getProfile()

    suspend fun getSettings() = ApiClient.apiReddit.getSettings()
}