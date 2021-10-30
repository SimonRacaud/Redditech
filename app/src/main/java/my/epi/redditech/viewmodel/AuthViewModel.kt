package my.epi.redditech.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import my.epi.redditech.model.api.OAuthModel
import my.epi.redditech.repository.AppRepository

/**
 * Network - Auth page
 */
class AuthViewModel constructor(private val appRepository:
                                     AppRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val oauthToken = MutableLiveData<OAuthModel>()
    private var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    fun getToken(code : String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = appRepository.getToken(code)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    oauthToken.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : $${response.message()}")
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