package com.mihan.movie.library.presentation.navigation

import androidx.navigation.NavOptionsBuilder

fun NavOptionsBuilder.popUpToExit() {
    popUpTo(Screens.Splash.route) {
        inclusive = true
        saveState = false
    }
}

fun NavOptionsBuilder.popUpToMain() {
    popUpTo(Screens.Home.route) {
        inclusive = false
    }
    launchSingleTop = true
}