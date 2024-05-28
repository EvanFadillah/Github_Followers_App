package com.ervan.github.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity (tableName = "favourite_user")
data class FavouriteUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "login")
    var login: String = "",
    @ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null,
) : Parcelable