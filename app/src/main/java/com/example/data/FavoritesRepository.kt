package com.example.data

import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val favoriteDao: FavoriteDao) {
    val allFavorites: Flow<List<FavoriteWallpaper>> = favoriteDao.getAllFavorites()

    suspend fun addFavorite(favorite: FavoriteWallpaper) {
        favoriteDao.insertFavorite(favorite)
    }

    suspend fun removeFavorite(url: String) {
        favoriteDao.deleteFavoriteByUrl(url)
    }

    fun isFavorite(url: String): Flow<Boolean> {
        return favoriteDao.isFavorite(url)
    }
}
