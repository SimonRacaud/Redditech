package my.epi.redditech.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import my.epi.redditech.MainActivity
import my.epi.redditech.R
import my.epi.redditech.fragments.LoginFragment

class AuthActivity(val context: MainActivity) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        this.createWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun createWebView() {
        // Inject login fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, LoginFragment(context))
        transaction.addToBackStack(null)
        transaction.commit()
    }

}