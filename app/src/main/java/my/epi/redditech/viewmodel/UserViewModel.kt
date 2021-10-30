package my.epi.redditech.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import my.epi.redditech.model.api.UserModel
import my.epi.redditech.repository.AppRepository

/**
 * Network - Settings page - User account
 */
class UserViewModel constructor(private val appRepository:
                                AppRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val user = MutableLiveData<UserModel>()
    private var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    fun getUser() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = appRepository.getProfile()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    user.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()}")
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