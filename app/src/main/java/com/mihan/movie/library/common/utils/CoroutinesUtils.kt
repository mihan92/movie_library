package com.mihan.movie.library.common.utils

import kotlinx.coroutines.flow.SharingStarted

private const val StopTimeoutMillis: Long = 5000L

val whileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)