package com.mihan.movie.library.common.extentions

import android.database.Cursor
import com.mihan.movie.library.common.Constants

fun Cursor.getColumn(columnIndex: String) = this.getInt(this.getColumnIndex(columnIndex) ?: Constants.DEFAILT_INT)