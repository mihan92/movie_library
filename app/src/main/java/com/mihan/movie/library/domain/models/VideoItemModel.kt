package com.mihan.movie.library.domain.models

data class VideoItemModel(
    val title: String,
    val category: String,
    val imageUrl: String,
    val videoUrl: String
)