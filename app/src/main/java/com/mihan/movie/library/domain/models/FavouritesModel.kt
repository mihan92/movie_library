package com.mihan.movie.library.domain.models

import com.mihan.movie.library.data.models.FavouritesDbModel

data class FavouritesModel(
    val videoId: String,
    val videoPageUrl: String,
    val videoTitle: String,
    val posterUrl: String
)

fun FavouritesModel.toFavouritesDbModel() = FavouritesDbModel(
    videoId = videoId,
    videoPageUrl = videoPageUrl,
    videoTitle = videoTitle,
    posterUrl = posterUrl
)
