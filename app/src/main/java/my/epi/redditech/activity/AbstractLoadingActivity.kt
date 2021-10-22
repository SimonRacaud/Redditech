package my.epi.redditech.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import my.epi.redditech.R
import my.epi.redditech.fragment.LoadingFragment

abstract class AbstractLoadingActivity : AppCompatActivity() {
    private var loadingCounter = 0

    fun startLoading() {
        loadingCounter++
        if (loadingCounter == 1) {
            supportFragmentManager.beginTransaction()
                .add(R.id.loading_frag_container, LoadingFragment::class.java, null)
                .commit()
        }
    }

    fun stopLoading() {
        if (loadingCounter > 0) {
            loadingCounter--
            if (loadingCounter == 0) {
                val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.loading_frag_container)
                if (fragment != null) {
                    supportFragmentManager.beginTransaction().remove(fragment).commit()
                }
            }
        }
    }
}