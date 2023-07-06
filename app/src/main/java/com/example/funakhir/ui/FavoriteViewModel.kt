package com.example.funakhir.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.funakhir.data.FavoriteDB
import com.example.funakhir.data.FavoriteDao
import com.example.funakhir.model.UsersItem

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private var favoriteDao: FavoriteDao?
    private var favoriteDB: FavoriteDB?

    init {
        favoriteDB = FavoriteDB.getDatabase(application)
        favoriteDao = favoriteDB?.favoriteDao()
    }

    fun getFavoriteList(): LiveData<List<UsersItem>>? {
        return favoriteDao?.getFavoriteList()
    }
}