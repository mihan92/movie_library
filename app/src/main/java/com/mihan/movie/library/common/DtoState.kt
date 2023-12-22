package com.mihan.movie.library.common

import com.mihan.movie.library.common.Constants.EMPTY_STRING

sealed class DtoState<T>(val data: T? = null, val errorMessage: String = EMPTY_STRING) {
    class Loading<T>: DtoState<T>()
    class Success<T>(data: T?): DtoState<T>(data = data)
    class Error<T>(errorMessage: String): DtoState<T>(errorMessage = errorMessage)
}
