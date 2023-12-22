package com.mihan.movie.library.data.models

import com.mihan.movie.library.common.Constants.EMPTY_STRING
import com.mihan.movie.library.domain.models.VideoDetailModel

data class VideoDetailDto(
    val title: String = EMPTY_STRING,
    val description: String = EMPTY_STRING,
    val releaseDate: String = EMPTY_STRING,
    val country: String = EMPTY_STRING,
    val ratingIMdb: String = EMPTY_STRING,
    val ratingHdrezka: String = EMPTY_STRING,
    val genre: String = EMPTY_STRING,
    val imageUrl: String = EMPTY_STRING,
)

fun VideoDetailDto.toVideoDetail() = VideoDetailModel(
    title = title,
    description = description,
    releaseDate = releaseDate,
    country = country,
    ratingIMdb = ratingIMdb,
    ratingHdrezka = ratingHdrezka,
    genre = genre,
    imageUrl = imageUrl
)
