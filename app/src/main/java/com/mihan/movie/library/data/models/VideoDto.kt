package com.mihan.movie.library.data.models

import com.mihan.movie.library.common.Constants.EMPTY_STRING
import com.mihan.movie.library.domain.models.VideoModel

data class VideoDto(
    val videoId: String = EMPTY_STRING,
    val isVideoHasTranslations: Boolean = false,
    val isVideoHasSeries: Boolean = false,
    val translations: Map<String, String> = emptyMap(),
    val videoStreamsWithTranslatorName: Map<String, List<StreamDto>> = emptyMap(),
    val seasonList: Map<String, List<String>> = emptyMap()
)

fun List<StreamDto>.toListStreamModel() = map(StreamDto::toStreamModel)

fun VideoDto.toVideoModel() = VideoModel(
    videoId = videoId,
    isVideoHasTranslations = isVideoHasTranslations,
    isVideoHasSeries = isVideoHasSeries,
    translations = translations,
    videoStreamsWithTranslatorName = videoStreamsWithTranslatorName.mapValues { it.value.toListStreamModel() },
    seasonList = seasonList
)