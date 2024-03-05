package com.mihan.movie.library.domain

import com.mihan.movie.library.data.models.FavouritesDbModel
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {

    fun getFavourites(): Flow<List<FavouritesDbModel>>

    fun getFavouriteById(videoId: String): Flow<FavouritesDbModel?>

    suspend fun addToFavourites(favouritesDbModel: FavouritesDbModel)

    suspend fun deleteFromFavourites(videoId: String)
}