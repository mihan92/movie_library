package com.mihan.movie.library.data.repository

import com.mihan.movie.library.data.local.db.FavouritesDao
import com.mihan.movie.library.data.models.FavouritesDbModel
import com.mihan.movie.library.domain.FavouritesRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class FavouritesRepositoryImpl @Inject constructor(
    private val favouritesDao: FavouritesDao
): FavouritesRepository {

    override fun getFavourites(): Flow<List<FavouritesDbModel>> {
        return favouritesDao.getFavourites()
    }

    override fun getFavouriteById(videoId: String): Flow<FavouritesDbModel?> {
        return favouritesDao.getFavouriteVideoById(videoId)
    }

    override suspend fun addToFavourites(favouritesDbModel: FavouritesDbModel) {
        favouritesDao.addToFavourites(favouritesDbModel)
    }

    override suspend fun deleteFromFavourites(videoId: String) {
        favouritesDao.deleteFromFavourites(videoId)
    }
}