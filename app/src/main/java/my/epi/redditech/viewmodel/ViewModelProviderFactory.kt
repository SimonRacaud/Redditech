package my.epi.redditech.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.epi.redditech.activity.PostPageActivity
import my.epi.redditech.repository.AppRepository
import java.lang.IllegalArgumentException

/**
 * Network access classes registration
 */
class ViewModelProviderFactory(
    private val appRepository: AppRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(this.appRepository) as T
        }
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(this.appRepository) as T
        }
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(this.appRepository) as T
        }
        if (modelClass.isAssignableFrom(HomeSubredditsViewModel::class.java)) {
            return HomeSubredditsViewModel(this.appRepository) as T
        }
        if (modelClass.isAssignableFrom(HomePostsViewModel::class.java)) {
            return HomePostsViewModel(this.appRepository) as T
        }
        if (modelClass.isAssignableFrom(SubredditViewModel::class.java)) {
            return SubredditViewModel(this.appRepository) as T
        }
        if (modelClass.isAssignableFrom(PostPageViewModel::class.java)) {
            return PostPageViewModel(this.appRepository) as T
        }
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(this.appRepository) as T
        }
        throw IllegalArgumentException("ViewModel Not Found")
    }
}