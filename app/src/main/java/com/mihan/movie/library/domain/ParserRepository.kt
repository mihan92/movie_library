package com.mihan.movie.library.domain

import com.mihan.movie.library.common.entites.Filter
import com.mihan.movie.library.common.entites.VideoCategory
import com.mihan.movie.library.data.models.SeasonModelDto
import com.mihan.movie.library.data.models.StreamDto
import com.mihan.movie.library.data.models.VideoDetailDto
import com.mihan.movie.library.data.models.VideoDto
import com.mihan.movie.library.data.models.VideoItemDto

interface ParserRepository {
    suspend fun getListVideo(filter: Filter, videoCategory: VideoCategory, page: Int, baseUrl: String): List<VideoItemDto>

    suspend fun getDetailVideoByUrl(url: String): VideoDetailDto

    suspend fun getTranslationsByUrl(url: String): VideoDto

    suspend fun getStreamsBySeasonId(translationId: String, videoId: String, season: String, episode: String): List<StreamDto>

    suspend fun getVideosByTitle(videoTitle: String): List<VideoItemDto>

    suspend fun getSeasonsByTranslatorId(translatorId: String): List<SeasonModelDto>
}