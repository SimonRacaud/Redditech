package my.epi.redditech.activity

import android.net.Uri
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
import android.widget.Switch
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import my.epi.redditech.model.api.PrefModel
import my.epi.redditech.model.api.UserModel
import my.epi.redditech.utils.ErrorMessage
import my.epi.redditech.utils.LoadingManager
import my.epi.redditech.viewmodel.SettingsViewModel
import java.lang.Exception


class SettingsActivity : AppCompatActivity() {
    private lateinit var loadingManager: LoadingManager
    private lateinit var userViewModel: UserViewModel
    private lateinit var prefViewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding
    private var userModel: UserModel? = null
    private var prefModel: PrefModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        loadingManager = LoadingManager(supportFragmentManager)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_settings
        )
        init()

        /// BACK BUTTON
        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }
        /// Settings
        val switchFollower = findViewById<Switch>(R.id.switch_follow)
        switchFollower.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            prefModel!!.enable_followers = isChecked
            prefViewModel.setSettings(prefModel!!)
        })
        val switchUnsubEmail = findViewById<Switch>(R.id.switch_unsub_email)
        switchUnsubEmail.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            prefModel!!.email_unsubscribe_all = isChecked
            prefViewModel.setSettings(prefModel!!)
        })
        val switchAdult = findViewById<Switch>(R.id.switch_adult)
        switchAdult.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            prefModel!!.over_18 = isChecked
            prefViewModel.setSettings(prefModel!!)
        })
        val switchAdultSearch = findViewById<Switch>(R.id.switch_adult_search)
        switchAdultSearch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            prefModel!!.search_include_over_18 = isChecked
            prefViewModel.setSettings(prefModel!!)
        })
        val switchAutoplay = findViewById<Switch>(R.id.switch_video_autoplay)
        switchAutoplay.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            prefModel!!.video_autoplay = isChecked
            prefViewModel.setSettings(prefModel!!)
        })
        val switchTpdpa = findViewById<Switch>(R.id.switch_data_perso_ads)
        switchTpdpa.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            prefModel!!.third_party_data_personalized_ads = isChecked
            prefViewModel.setSettings(prefModel!!)
        })
        val switchTppa = findViewById<Switch>(R.id.switch_perso_ads)
        switchTppa.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            prefModel!!.third_party_personalized_ads = isChecked
            prefViewModel.setSettings(prefModel!!)
        })
        val switchTpsdpa = findViewById<Switch>(R.id.switch_site_data_perso_ads)
        switchTpsdpa.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            prefModel!!.third_party_site_data_personalized_ads = isChecked
            prefViewModel.setSettings(prefModel!!)
        })
        val switchTpsdpc = findViewById<Switch>(R.id.switch_site_data_perso_content)
        switchTpsdpc.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            prefModel!!.third_party_site_data_personalized_content = isChecked
            prefViewModel.setSettings(prefModel!!)
        })
    }

    private fun init() {
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        prefViewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)
        try {
            this.getUser()
            this.getSettings()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        userViewModel.getUser()
        prefViewModel.getSettings()
    }

    private fun getUser() {
        loadingManager.startLoading()
        userViewModel.user.observe(this, {
            /// Loading finished
            binding.user = it

            if (binding.user!!.icon_img != "") {
                val imageView = findViewById<ImageView>(R.id.account_picture)
                val pictureUrl = binding.user!!.icon_img.toString().replace("&amp;", "&")
                Glide.with(this).load(Uri.parse(pictureUrl)).into(imageView)
            }
            userModel = binding.user
        })
        userViewModel.errorMessage.observe(this, {
            ErrorMessage.show(this, it)
            loadingManager.stopLoading()
        })
        userViewModel.loading.observe(this, {
            if (it) {
                // it show progress bar (loading...)
            } else {
                loadingManager.stopLoading()
            }
        })
    }

    private fun getSettings() {
        loadingManager.startLoading()
        prefViewModel.preferences.observe(this, {
            /// Loading finished
            binding.preferences = it
            prefModel = binding.preferences
        })
        userViewModel.errorMessage.observe(this, {
            ErrorMessage.show(this, it)
            loadingManager.stopLoading()
        })
        userViewModel.loading.observe(this, {
            if (it) {
                // it show progress bar (loading...)
            } else {
                loadingManager.stopLoading()
            }
        })
    }
}