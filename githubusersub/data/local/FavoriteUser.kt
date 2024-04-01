package com.example.githubusersub.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "user")
@Parcelize
data class FavoriteUser(
    @ColumnInfo(name = "login")
    val login: String,
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "avatarUrl")
    val avatarUrl: String
) : Parcelable
