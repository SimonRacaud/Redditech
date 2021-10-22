package my.epi.redditech.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import my.epi.redditech.model.api.ListModel
import my.epi.redditech.model.api.SubredditModel
import my.epi.redditech.repository.AppRepository

class HomeSubredditsViewModel constructor(private val appRepository:
                                          AppRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val subredditList = MutableLiveData<ListModel<SubredditModel>>()
    private var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    fun getSubscribedSubreddit() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = appRepository.getSubscribedSubreddit()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    subredditList.postValue(response.body())
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