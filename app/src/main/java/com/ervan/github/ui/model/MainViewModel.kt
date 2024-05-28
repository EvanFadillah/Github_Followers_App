package com.ervan.github.ui.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ervan.github.data.database.FavouriteUser
import com.ervan.github.data.retrofit.ApiClient
import com.ervan.github.repository.FavouriteRepository
import com.ervan.github.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(val mFavouriteRepository: FavouriteRepository) : ViewModel() {
    val resultUser = MutableLiveData<Result>()

    fun getAll(): LiveData<List<FavouriteUser>> = mFavouriteRepository.getAll()

 fun getUser() {
     viewModelScope.launch(Dispatchers.IO) {
         launch(Dispatchers.Main) {
             flow {
                 val response = ApiClient
                     .apiService
                     .getUserGithub()

                 emit(response)
             }.onStart {
                 resultUser.value = Result.Loading(true)
             }.onCompletion {
                 resultUser.value = Result.Loading(false)
             }.catch {
                 Log.e(TAG, it.message.toString())
                 it.printStackTrace()
                 resultUser.value = Result.Error(it)
             }.collect {
                resultUser.value = Result.Success(it)
             }
         }
     }
 }

    fun getUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                flow {
                    val response = ApiClient
                        .apiService
                        .searchUserGithub(
                            mapOf(
                                "q" to username,
                                "per_page" to 10
                            )
                        )

                    emit(response)
                }.onStart {
                    resultUser.value = Result.Loading(true)
                }.onCompletion {
                    resultUser.value = Result.Loading(false)
                }.catch {
                    Log.e(TAG, it.message.toString())
                    it.printStackTrace()
                    resultUser.value = Result.Error(it)
                }.collect {
                    resultUser.value = Result.Success(it.items)
                }
            }
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}