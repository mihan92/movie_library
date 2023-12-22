package com.mihan.movie.library.presentation.screens.detail

import com.mihan.movie.library.common.Constants.EMPTY_STRING
import com.mihan.movie.library.domain.models.VideoDetailModel

data class DetailScreenState(
    val isLoading: Boolean = false,
    val detailInfo: VideoDetailModel? = null,
    val errorMessage: String = EMPTY_STRING
)
