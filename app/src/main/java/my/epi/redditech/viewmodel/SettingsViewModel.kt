package my.epi.redditech.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.model.api.PrefModel

/**
 * Network - settings page
 */
class SettingsViewModel constructor(private val appRepository:
                                    AppRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val preferences = MutableLiveData<PrefModel>()
    private var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    fun getSettings() {
        job = CoroutineScope(Dispatchers.IO).launch {
           val response = appRepository.getSettings()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    preferences.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    fun setSettings(pref: PrefModel) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = appRepository.setSettings(pref)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    /// TODO settings applied success
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}