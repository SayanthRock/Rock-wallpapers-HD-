package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_wallpapers ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteWallpaper>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteWallpaper)

    @Query("DELETE FROM favorite_wallpapers WHERE url = :url")
    suspend fun deleteFavoriteByUrl(url: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_wallpapers WHERE url = :url LIMIT 1)")
    fun isFavorite(url: String): Flow<Boolean>
}
