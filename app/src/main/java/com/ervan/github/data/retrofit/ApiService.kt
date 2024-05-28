package com.ervan.github.data.retrofit

import com.ervan.github.BuildConfig
import com.ervan.github.data.response.DetailResponse
import com.ervan.github.data.response.ItemsItem
import com.ervan.github.data.response.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    @JvmSuppressWildcards
    @GET ("users")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    suspend fun getUserGithub(): MutableList<ItemsItem>

    @JvmSuppressWildcards
    @GET ("users/{username}")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    suspend fun getDetailGithub(
        @Path("username") username: String
    ): DetailResponse

    @JvmSuppressWildcards
    @GET ("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    suspend fun getFollowersGithub(
        @Path("username") username: String
    ): MutableList<ItemsItem>

    @JvmSuppressWildcards
    @GET ("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    suspend fun getFollowingGithub(
        @Path("username") username: String
    ): MutableList<ItemsItem>

    @JvmSuppressWildcards
    @GET ("search/users")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    suspend fun searchUserGithub(
        @QueryMap params: Map<String, Any>,

    ): SearchResponse
}
