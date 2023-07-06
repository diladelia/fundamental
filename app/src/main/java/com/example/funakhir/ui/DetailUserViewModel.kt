package com.example.funakhir.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.funakhir.data.ApiConfig
import com.example.funakhir.data.FavoriteDB
import com.example.funakhir.data.FavoriteDao
import com.example.funakhir.model.UsersItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {

    val listDetailUsers = MutableLiveData<UsersItem>()

    private var favoriteDao: FavoriteDao?
    private var favoriteDB: FavoriteDB?

    init {
        favoriteDB = FavoriteDB.getDatabase(application)
        favoriteDao = favoriteDB?.favoriteDao()
    }

    fun setDetailUser(username: String) {
        ApiConfig.getApiService()
            .getUserDetail(username)
            .enqueue(object : Callback<UsersItem> {
                override fun onResponse(
                    call: Call<UsersItem>,
                    response: Response<UsersItem>
                ) {
                    if (response.isSuccessful) {
                        listDetailUsers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<UsersItem>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun getDetailUser(): LiveData<UsersItem> {
        return listDetailUsers
    }

    fun insertToFavorite(user: UsersItem) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteDao?.insertToFavorite(user)
        }
    }

    suspend fun getFavoriteDetail(username: String) = favoriteDao?.getFavoriteDetail(username)

    fun deleteFromFavorite(user: UsersItem) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteDao?.deleteFromFavorite(user)
        }
    }
}