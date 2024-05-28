package com.ervan.github.ui

import SettingPreference
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ervan.github.data.di.Injection
import com.ervan.github.repository.FavouriteRepository
import com.ervan.github.ui.model.DetailViewModel
import com.ervan.github.ui.model.FavouriteViewModel
import com.ervan.github.ui.model.MainViewModel
import com.ervan.github.ui.model.SettingViewModel
import dataStore

class ViewModelFactory private constructor(private val newsRepository: FavouriteRepository,
    private val pref: SettingPreference) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(newsRepository) as T
        }

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(newsRepository) as T
        }

        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            return FavouriteViewModel(newsRepository) as T
        }

        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(application: Application): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(application.applicationContext),
                    SettingPreference.getInstance(application.dataStore))
            }.also { instance = it }
    }
}