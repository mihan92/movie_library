package com.mihan.hdrezka.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HDrezkaTheme(
    content: @Composable () -> Unit,
) {
    val colorScheme = darkColorScheme(
        primary = primaryColor,
        secondary = secondaryColor,
        background = backgroundColor
    )
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}