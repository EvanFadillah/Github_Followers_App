package com.ervan.github.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavouriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouriteUser: FavouriteUser)

    @Query("SELECT * FROM favourite_user")
    fun getAll() : LiveData<List<FavouriteUser>>

    @Query("SELECT * FROM favourite_user WHERE favourite_user.login = :login LIMIT 1")
    fun findByLogin(login: String): FavouriteUser?

    @Query("DELETE FROM favourite_user WHERE favourite_user.login = :login")
     suspend fun removedFromFavourite(login: String): Int

    @Delete
     suspend fun delete(favouriteUser: FavouriteUser)

}