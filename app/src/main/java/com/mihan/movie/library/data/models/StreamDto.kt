package com.mihan.movie.library.data.models

import com.mihan.movie.library.domain.models.StreamModel

data class StreamDto(
    val url: String,
    val quality: String
)

fun StreamDto.toStreamModel() = StreamModel(
    url = url,
    quality = quality
)
