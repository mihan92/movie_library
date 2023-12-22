package com.mihan.movie.library.domain.models

data class VideoDetailModel(
    val title: String,
    val description: String,
    val releaseDate: String,
    val country: String,
    val ratingIMdb: String,
    val ratingHdrezka: String,
    val genre: String,
    val imageUrl: String,
)
