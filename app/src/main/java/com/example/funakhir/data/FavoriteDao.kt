package com.example.funakhir.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.funakhir.model.UsersItem

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToFavorite(user: UsersItem)

    @Query("SELECT * FROM user")
    fun getFavoriteList(): LiveData<List<UsersItem>>

    @Query("SELECT count(*) FROM user WHERE user.username = :username")
    suspend fun getFavoriteDetail(username: String): Int

    @Delete
    suspend fun deleteFromFavorite(user: UsersItem)
}