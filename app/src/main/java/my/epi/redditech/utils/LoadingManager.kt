package my.epi.redditech.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import my.epi.redditech.R
import my.epi.redditech.fragment.LoadingFragment

class LoadingManager(val manager: FragmentManager) {
    private var loadingCounter = 0

    fun startLoading() {
        loadingCounter++
        if (loadingCounter == 1) {
            manager.beginTransaction()
                .add(R.id.loading_frag_container, LoadingFragment::class.java, null)
                .commit()
        }
    }

    fun stopLoading() {
        if (loadingCounter > 0) {
            loadingCounter--
            if (loadingCounter == 0) {
                val fragment: Fragment? = manager.findFragmentById(R.id.loading_frag_container)
                if (fragment != null) {
                    manager.beginTransaction().remove(fragment).commit()
                }
            }
        }
    }
}