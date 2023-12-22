package com.mihan.movie.library.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.tv.material3.Text
import com.mihan.movie.library.presentation.animation.AnimatedScreenTransitions
import com.ramcosta.composedestinations.annotation.Destination

@Destination(style = AnimatedScreenTransitions::class)
@Composable
fun PlaceholderScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Этот экран находится на стадии разработки")
    }
}