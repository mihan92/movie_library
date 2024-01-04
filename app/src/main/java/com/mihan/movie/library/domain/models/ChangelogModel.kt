package com.mihan.movie.library.domain.models

data class ChangelogModel(
    val latestVersion: String,
    val latestVersionCode: Int,
    val apkUrl: String,
    val releaseNotes: List<String>
)
