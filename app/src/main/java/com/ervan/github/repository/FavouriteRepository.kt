package com.ervan.github.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ervan.github.data.AppExecutors
import com.ervan.github.data.database.FavouriteUser
import com.ervan.github.data.database.FavouriteUserDao
import com.ervan.github.data.database.FavouriteUserDatabase
import com.ervan.github.data.retrofit.ApiService
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class FavouriteRepository( val apiService: ApiService,
                           val favouriteUserDao: FavouriteUserDao,
                           val appExecutors: AppExecutors) {

    fun getAll(): LiveData<List<FavouriteUser>> = favouriteUserDao.getAll()

    suspend fun insert(favouriteUser: FavouriteUser) {
        favouriteUserDao.insert(favouriteUser)
    }

    suspend fun delete(favouriteUser: FavouriteUser) {
        favouriteUserDao.delete(favouriteUser)
    }

     fun findByLogin(login: String) : FavouriteUser? {
        return favouriteUserDao.findByLogin(login)
    }

    suspend fun removeFromFavourite(login: String): Int {
        return favouriteUserDao.removedFromFavourite(login)
    }

    companion object {
        @Volatile
        private var instance: FavouriteRepository? = null
        fun getInstance(
            apiService: ApiService,
            favouriteUserDao: FavouriteUserDao,
            appExecutors: AppExecutors
        ): FavouriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavouriteRepository(apiService, favouriteUserDao, appExecutors)
            }.also { instance = it }
    }
}
