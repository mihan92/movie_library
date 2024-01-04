package com.mihan.movie.library.data.models

import com.mihan.movie.library.common.Constants
import com.mihan.movie.library.domain.models.ChangelogModel

data class ChangelogDto(
    val latestVersion: String = Constants.EMPTY_STRING,
    val latestVersionCode: Int = Constants.DEFAILT_INT,
    val apkUrl: String = Constants.EMPTY_STRING,
    val releaseNotes: List<String> = emptyList()
)

fun ChangelogDto.toChangelogModel() = ChangelogModel(
    latestVersion = latestVersion,
    latestVersionCode = latestVersionCode,
    apkUrl = apkUrl,
    releaseNotes = releaseNotes
)
