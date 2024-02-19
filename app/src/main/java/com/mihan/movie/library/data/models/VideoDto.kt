package com.mihan.movie.library.data.models

import com.mihan.movie.library.common.Constants.EMPTY_STRING
import com.mihan.movie.library.domain.models.VideoModel

data class VideoDto(
    val videoId: String = EMPTY_STRING,
    val isVideoHasTranslations: Boolean = false,
    val isVideoHasSeries: Boolean = false,
    val translations: Map<String, String> = emptyMap()
)

fun VideoDto.toVideoModel() = VideoModel(
    videoId = videoId,
    isVideoHasTranslations = isVideoHasTranslations,
    isVideoHasSeries = isVideoHasSeries,
    translations = translations
)