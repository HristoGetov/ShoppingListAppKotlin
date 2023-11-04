package com.example.shoppinglistaappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglistaappkotlin.data.DataStoreManager
import com.example.shoppinglistaappkotlin.databinding.ActivitySettingsBinding
import com.example.shoppinglistaappkotlin.presentation.MainActivity
import com.example.shoppinglistaappkotlin.presentation.viewModel.DataStoreViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivitySettingsBinding

    private lateinit var dataStoreViewModel: DataStoreViewModel
    private lateinit var dataStoreManager: DataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        dataBinding.apply {
            backButton.setOnClickListener {
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            dataStoreViewModel = ViewModelProvider(this@SettingsActivity)[DataStoreViewModel::class.java]
            dataStoreManager = DataStoreManager(this@SettingsActivity)
            checkLanguage()
            langSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                Log.e("Log", "isChecked: $isChecked")
                if (isChecked) {
                    setLocale("en-En")
                } else {
                    setLocale("bg-BG")
                }
            })
            langSwitch.setOnCheckedChangeListener{ _ , isChecked ->
                when(isChecked){
                    true->{
                        dataStoreViewModel.setLanguage(false)
                    }false ->{
                    dataStoreViewModel.setLanguage(true)
                }
                }
            }
        }

    }

    private fun checkLanguage() {
        dataBinding.apply {
            dataStoreViewModel.getLanguage.observe(this@SettingsActivity) { isBGLannguage ->
                when (isBGLannguage) {
                    true -> {
                        setLocale("bg-BG")
                        langSwitch.isChecked = false
                    }
                    false -> {
                        setLocale("en-EN")
                        langSwitch.isChecked = true
                    }
                }
            }
        }

    }

    private fun setLocale(language: String?) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}