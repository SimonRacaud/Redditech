package my.epi.redditech.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.epi.redditech.repository.AppRepository
import java.lang.IllegalArgumentException

class ViewModelProviderFactory(
    private val appRepository: AppRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(this.appRepository) as T
        }
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(this.appRepository) as T
        }
        if (modelClass.isAssignableFrom(HomeSubredditsViewModel::class.java)) {
            return HomeSubredditsViewModel(this.appRepository) as T
        }
        throw IllegalArgumentException("ViewModel Not Found")
    }
}