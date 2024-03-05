package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.data.models.toFavouritesModel
import com.mihan.movie.library.domain.FavouritesRepository
import com.mihan.movie.library.domain.models.FavouritesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(private val favouritesRepository: FavouritesRepository) {

    operator fun invoke(): Flow<List<FavouritesModel>> =
        favouritesRepository.getFavourites().map { list -> list.map { it.toFavouritesModel() } }
}