package com.mihan.movie.library.presentation.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

private const val TWEEN_DURATION_IN_MILLIS = 500
private const val POSITIVE_OFFSET = 1000
private const val NEGATIVE_OFFSET = -1000

object AnimatedScreenTransitions : DestinationStyle.Animated {
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { POSITIVE_OFFSET },
            animationSpec = tween(TWEEN_DURATION_IN_MILLIS)
        ) + fadeIn()
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { NEGATIVE_OFFSET },
            animationSpec = tween(TWEEN_DURATION_IN_MILLIS)
        ) + fadeOut()
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { NEGATIVE_OFFSET },
            animationSpec = tween(TWEEN_DURATION_IN_MILLIS)
        ) + fadeIn()
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { POSITIVE_OFFSET },
            animationSpec = tween(TWEEN_DURATION_IN_MILLIS)
        ) + fadeOut()
    }
}