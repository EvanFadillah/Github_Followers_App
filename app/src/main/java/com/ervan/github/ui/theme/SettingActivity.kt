package com.ervan.github.ui.theme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ervan.github.databinding.ActivityThemeBinding
import com.ervan.github.ui.ViewModelFactory
import com.ervan.github.ui.model.SettingViewModel
import dataStore

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThemeBinding
    private val viewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switchTheme = binding.switchTheme

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
            switchTheme.isChecked = isChecked
        }

        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            val mode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
            switchTheme.isChecked = isDarkModeActive
        }
    }
}