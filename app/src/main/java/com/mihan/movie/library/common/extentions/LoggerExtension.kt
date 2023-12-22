package com.mihan.movie.library.common.extentions

import android.util.Log

fun Any.logger(message: String, tag: String = this.javaClass.simpleName) {
    Log.d(tag, message)
}