package my.epi.redditech.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import my.epi.redditech.R
import my.epi.redditech.databinding.ActivitySettingsBinding
import my.epi.redditech.repository.AppRepository
import my.epi.redditech.viewmodel.SettingsViewModel
import my.epi.redditech.viewmodel.UserViewModel
import my.epi.redditech.viewmodel.ViewModelProviderFactory

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_settings)
        init()
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
            binding.user = it;
        })
        viewModel.errorMessage.observe(this, {
            //TODO: use it
        })
        viewModel.loading.observe(this, {
            if (it) {
                //TODO: it show progress bar
            } else {
                //TODO: it mask progress
            }
        })
    }
 }