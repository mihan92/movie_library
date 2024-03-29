package com.mihan.movie.library.domain.models

import com.mihan.movie.library.common.Constants

data class VideoModel(
    val videoId: String = Constants.EMPTY_STRING,
    val isVideoHasTranslations: Boolean = false,
    val isVideoHasSeries: Boolean = false,
    val translations: Map<String, String> = emptyMap()
)
