package com.mihan.movie.library.data.models

import com.mihan.movie.library.common.Constants.EMPTY_STRING
import com.mihan.movie.library.domain.models.VideoItemModel

data class VideoItemDto(
    val title: String = EMPTY_STRING,
    val category: String = EMPTY_STRING,
    val imageUrl: String = EMPTY_STRING,
    val videoUrl: String = EMPTY_STRING
)


fun VideoItemDto.toVideoItemModel() = VideoItemModel(
    title = title,
    category = category,
    imageUrl = imageUrl,
    videoUrl = videoUrl
)