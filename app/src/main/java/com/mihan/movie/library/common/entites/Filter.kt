package com.mihan.movie.library.common.entites

import androidx.annotation.StringRes
import com.mihan.movie.library.R

enum class Filter(@StringRes val titleResId: Int, val section: String) {
    Watching(R.string.filter_watching_title, "watching"),
    Popular(R.string.filter_popular_title, "popular"),
    Last(R.string.filter_last_title, "last"),
    Soon(R.string.filter_soon_title, "soon"),
}