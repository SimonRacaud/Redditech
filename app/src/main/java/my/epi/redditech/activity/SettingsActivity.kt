package my.epi.redditech.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import my.epi.redditech.model.api.PrefModel
import my.epi.redditech.model.api.UserModel
import my.epi.redditech.viewmodel.SettingsViewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var prefViewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding
    private var userModel: UserModel? = null
    private var prefModel: PrefModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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
        this.getUser()
        this.getSettings()
        userViewModel.getUser()
        prefViewModel.getSettings()
    }

    private fun getUser() {
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
            //TODO: use it (error loading)
        })
        userViewModel.loading.observe(this, {
            if (it) {
                //TODO: it show progress bar (loading...)
            } else {
                //TODO: it mask progress (no loading...)
            }
        })
    }

    private fun getSettings() {
        prefViewModel.preferences.observe(this, {
            /// Loading finished
            binding.preferences = it

            prefModel = binding.preferences
            // TODO set switch checked attribute
        })
        userViewModel.errorMessage.observe(this, {
            //TODO: use it (error loading)
        })
        userViewModel.loading.observe(this, {
            if (it) {
                //TODO: it show progress bar (loading...)
            } else {
                //TODO: it mask progress (no loading...)
            }
        })
    }
}