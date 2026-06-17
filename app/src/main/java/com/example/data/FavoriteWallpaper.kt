package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_wallpapers")
data class FavoriteWallpaper(
    @PrimaryKey val url: String,
    val title: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)
