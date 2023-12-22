package com.mihan.movie.library.presentation.screens.home

import com.mihan.movie.library.common.Constants.EMPTY_STRING
import com.mihan.movie.library.domain.models.VideoItemModel

data class HomeScreenState(
    val isLoading: Boolean = false,
    val data: List<VideoItemModel> = emptyList(),
    val errorMessage: String = EMPTY_STRING
)
