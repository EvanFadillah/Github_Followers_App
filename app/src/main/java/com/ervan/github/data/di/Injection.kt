package com.ervan.github.data.di

import android.content.Context
import com.ervan.github.data.AppExecutors
import com.ervan.github.data.database.FavouriteUserDatabase
import com.ervan.github.data.retrofit.ApiClient
import com.ervan.github.repository.FavouriteRepository

object Injection {
    fun provideRepository(context: Context): FavouriteRepository {
        val apiService = ApiClient.apiService
        val database = FavouriteUserDatabase.getDatabase(context)
        val dao = database.FavouriteUserDao()
        val appExecutors = AppExecutors()
        return FavouriteRepository.getInstance(apiService, dao, appExecutors)
    }
}