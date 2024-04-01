package com.example.githubusersub.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: FavoriteUser)

    @Query("SELECT * FROM User")
    fun loadAll(): LiveData<MutableList<FavoriteUser>>

    @Query("SELECT count(*) FROM User WHERE User.id = :id")
    fun findById(id: Int): Int

    @Delete
    fun delete(user: FavoriteUser)
}