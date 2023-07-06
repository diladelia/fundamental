package com.example.funakhir.data

import retrofit2.Call
import com.example.funakhir.model.GithubResponse
import com.example.funakhir.model.UsersItem
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {
    @GET("search/users")
    fun searchUser (
        @Query("q")
        query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getUserDetail (
        @Path("username")
        username: String
    ): Call<UsersItem>

    @GET("users/{username}/followers")
    fun getUserFollowers (
        @Path("username")
        username: String
    ): Call<ArrayList<UsersItem>>

    @GET("users/{username}/following")
    fun getUserFollowing (
        @Path("username")
        username: String
    ): Call<ArrayList<UsersItem>>
}