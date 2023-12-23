package com.mihan.movie.library.presentation.screens.splash

import com.mihan.movie.library.common.Constants

data class SplashScreenState(
    val success: Boolean = false,
    val errorMessage: String = Constants.EMPTY_STRING
)
