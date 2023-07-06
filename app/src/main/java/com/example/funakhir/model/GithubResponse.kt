package com.example.funakhir.model

import com.google.gson.annotations.SerializedName

data class GithubResponse(
    @field:SerializedName("items")
    val items: ArrayList<UsersItem>
)
