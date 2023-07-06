package com.example.funakhir.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funakhir.data.ApiConfig
import com.example.funakhir.model.UsersItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    val listFollowersUser = MutableLiveData<ArrayList<UsersItem>>()

    fun setFollowersUser(username: String) {
        ApiConfig.getApiService()
            .getUserFollowers(username)
            .enqueue(object: Callback<ArrayList<UsersItem>> {
                override fun onResponse(
                    call: Call<ArrayList<UsersItem>>,
                    response: Response<ArrayList<UsersItem>>
                ) {
                    if (response.isSuccessful) {
                        listFollowersUser.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<UsersItem>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun getFollowersUser(): LiveData<ArrayList<UsersItem>> {
        return listFollowersUser
    }
}