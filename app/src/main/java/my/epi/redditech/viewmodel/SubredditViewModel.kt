package my.epi.redditech.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import my.epi.redditech.model.api.ItemModel
import my.epi.redditech.model.api.PostModel
import my.epi.redditech.model.api.SubredditModel
import my.epi.redditech.repository.AppRepository

class SubredditViewModel constructor(private val appRepository:
                                     AppRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val subredditInfo = MutableLiveData<ItemModel<SubredditModel>>()
    private var job: Job? = null
    var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    fun getInfoSubreddit(subreddit : String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = appRepository.getSubredditInfo(subreddit)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    subredditInfo.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
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