package com.mihan.movie.library.domain.models

import com.mihan.movie.library.data.models.VideoHistoryDbModel

data class VideoHistoryModel(
    val videoId: String,
    val videoPageUrl: String,
    val videoTitle: String,
    val posterUrl: String,
    val translatorName: String,
    val translatorId: String,
    val season: String,
    val episode: String,
    val watchingTime: Long
)

fun VideoHistoryModel.toVideoHistoryDbModel() = VideoHistoryDbModel(
    videoId = videoId,
    videoPageUrl = videoPageUrl,
    videoTitle = videoTitle,
    posterUrl = posterUrl,
    translatorName = translatorName,
    translatorId = translatorId,
    season = season,
    episode = episode,
    watchingTime = watchingTime
)
