package my.epi.redditech.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import my.epi.redditech.R
import my.epi.redditech.databinding.ActivitySettingsBinding
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.viewmodel.UserViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_settings)
        init()

        /// BACK BUTTON
        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }
    }

    private fun init() {
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        getSettings()
        viewModel.getUser()
    }

    private fun getSettings() {
        viewModel.user.observe(this, {
            /// Loading finished
            binding.user = it

            val pictureUrl = binding.user!!.icon_img.toString().replace("&amp;","&")
            println("Picture path is $pictureUrl");
            if (pictureUrl != "") {
                val imageView = findViewById<ImageView>(R.id.account_picture)
                Glide.with(this).load(Uri.parse(pictureUrl)).into(imageView)
            }
        })
        viewModel.errorMessage.observe(this, {
            //TODO: use it (error loading)
        })
        viewModel.loading.observe(this, {
            if (it) {
                //TODO: it show progress bar (loading...)
            } else {
                //TODO: it mask progress (no loading...)
            }
        })
    }
 }