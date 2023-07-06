package com.example.funakhir.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class UsersItem(
    @field:SerializedName("avatar_url")
    @ColumnInfo(name = "avatarUrl")
    val avatarUrl: String? = "",

    @field:SerializedName("id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int? = 0,

    @field:SerializedName("login")
    @ColumnInfo(name = "username")
    val username: String? = "",

    @field:SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String? = "",

    @field:SerializedName("public_repos")
    @ColumnInfo(name = "repository")
    val repository: Int? = 0,

    @field:SerializedName("followers")
    @ColumnInfo(name = "followers")
    val followers: Int? = 0,

    @field:SerializedName("following")
    @ColumnInfo(name = "following")
    val following: Int? = 0,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean? = false

)