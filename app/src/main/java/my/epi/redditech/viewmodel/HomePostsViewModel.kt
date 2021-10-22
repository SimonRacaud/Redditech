package my.epi.redditech.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import my.epi.redditech.model.api.ListModel
import my.epi.redditech.model.api.PostModel
import my.epi.redditech.repository.AppRepository

class HomePostsViewModel constructor(private val appRepository:
                                    AppRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val postsList = MutableLiveData<ListModel<PostModel>>()
    private var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    fun getPostsFeed(sort : String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = appRepository.getPostsFeed(sort)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    postsList.postValue(response.body())
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