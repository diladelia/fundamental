package com.example.funakhir.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.funakhir.model.UsersItem

@Database(
    entities = [UsersItem::class],
    version = 2
)

abstract class FavoriteDB: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteDB? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteDB {
            if (INSTANCE == null) {
                synchronized(FavoriteDB::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteDB::class.java, "favorite_db")
                        .build()
                }
            }
            return INSTANCE as FavoriteDB
        }
    }
}