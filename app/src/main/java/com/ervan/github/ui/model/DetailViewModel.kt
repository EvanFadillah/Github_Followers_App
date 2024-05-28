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

class DetailViewModel(val mFavouriteRepository: FavouriteRepository) : ViewModel() {
    val resultDetailUser = MutableLiveData<Result>()
    val resultFollowersUser = MutableLiveData<Result>()
    val resultFollowingUser = MutableLiveData<Result>()

    private var _favUser = MutableLiveData<FavouriteUser?>()
    var favUser: LiveData<FavouriteUser?> = _favUser

    fun insert(favouriteUser: FavouriteUser) {
        viewModelScope.launch(Dispatchers.IO) {
                mFavouriteRepository.insert(favouriteUser)
        }
    }

    fun delete(favouriteUser: FavouriteUser) {
        viewModelScope.launch(Dispatchers.IO) {
                mFavouriteRepository.delete(favouriteUser)
        }
    }

    fun findByLogin(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _favUser.postValue(mFavouriteRepository.findByLogin(login))
        }
    }

    fun removeFromFavourite(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
                mFavouriteRepository.removeFromFavourite(login)
        }
    }

    fun getDetailUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                flow {
                    val response = ApiClient
                        .apiService
                        .getDetailGithub(username)

                    emit(response)
                }.onStart {
                    resultDetailUser.value = Result.Loading(true)
                }.onCompletion {
                    resultDetailUser.value = Result.Loading(false)
                }.catch {
                    Log.e(TAG, it.message.toString())
                    it.printStackTrace()
                    resultDetailUser.value = Result.Error(it)
                }.collect {
                    resultDetailUser.value = Result.Success(it)

                }
            }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                flow {
                    val response = ApiClient
                        .apiService
                        .getFollowersGithub(username)

                    emit(response)
                }.onStart {
                    resultFollowersUser.value = Result.Loading(true)
                }.onCompletion {
                    resultFollowersUser.value = Result.Loading(false)
                }.catch {
                    Log.e(TAG, it.message.toString())
                    it.printStackTrace()
                    resultFollowersUser.value = Result.Error(it)
                }.collect {
                    resultFollowersUser.value = Result.Success(it)
                }
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                flow {
                    val response = ApiClient
                        .apiService
                        .getFollowingGithub(username)

                    emit(response)
                }.onStart {
                    resultFollowingUser.value = Result.Loading(true)
                }.onCompletion {
                    resultFollowingUser.value = Result.Loading(false)
                }.catch {
                    Log.e(TAG, it.message.toString())
                    it.printStackTrace()
                    resultFollowingUser.value = Result.Error(it)
                }.collect {
                    resultFollowingUser.value = Result.Success(it)
                }
            }
        }
    }

    companion object {
        const val TAG = "DetailViewModel"
    }
}

