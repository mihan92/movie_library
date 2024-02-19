package com.mihan.movie.library.data.models

import com.mihan.movie.library.common.Constants
import com.mihan.movie.library.domain.models.BaseUrlModel

data class BaseUrlModelDto(
    val baseUrl: String = Constants.EMPTY_STRING
)

fun BaseUrlModelDto.toBaseUrlModel() = BaseUrlModel(baseUrl)
