package com.mihan.movie.library.data.repository

import com.mihan.movie.library.common.DataStorePrefs
import com.mihan.movie.library.common.entites.Filter
import com.mihan.movie.library.common.entites.VideoCategory
import com.mihan.movie.library.data.local.LocalRezkaParser
import com.mihan.movie.library.data.models.BaseUrlModelDto
import com.mihan.movie.library.data.models.SeasonModelDto
import com.mihan.movie.library.data.models.StreamDto
import com.mihan.movie.library.data.models.VideoDetailDto
import com.mihan.movie.library.data.models.VideoDto
import com.mihan.movie.library.data.models.VideoItemDto
import com.mihan.movie.library.data.remote.RemoteParserApiService
import com.mihan.movie.library.domain.ParserRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@ActivityRetainedScoped
class ParserRepositoryImpl @Inject constructor(
    private val remoteParser: RemoteParserApiService,
    private val localParser: LocalRezkaParser,
    private val dataStorePrefs: DataStorePrefs
) : ParserRepository {
    override suspend fun getListVideo(filter: Filter, videoCategory: VideoCategory, page: Int): List<VideoItemDto> {
        return if (dataStorePrefs.getRemoteParsing().first())
            remoteParser.getListVideo(filter.section, videoCategory.genre, page)
        else
            localParser.getListVideo(filter, videoCategory, page)
    }

    override suspend fun getDetailVideoByUrl(url: String): VideoDetailDto {
        return if (dataStorePrefs.getRemoteParsing().first())
            remoteParser.getDetailVideoByUrl(url)
        else
            localParser.getDetailVideoByUrl(url)
    }

    override suspend fun getTranslationsByUrl(url: String): VideoDto {
        return if (dataStorePrefs.getRemoteParsing().first())
            remoteParser.getTranslationsByUrl(url)
        else
            localParser.getTranslationsByUrl(url)
    }

    override suspend fun getVideosByTitle(videoTitle: String): List<VideoItemDto> {
        return if (dataStorePrefs.getRemoteParsing().first())
            remoteParser.getVideosByTitle(videoTitle)
        else
            localParser.getVideosByTitle(videoTitle)
    }

    override suspend fun getSeasonsByTranslatorId(translatorId: String): List<SeasonModelDto> {
        return if (dataStorePrefs.getRemoteParsing().first())
            remoteParser.getSeasonsByTranslatorId(translatorId)
        else
            localParser.getSeasonsByTranslatorId(translatorId)
    }

    override suspend fun updateBaseUrl(baseUrl: String) {
        if (dataStorePrefs.getRemoteParsing().first())
            remoteParser.updateBaseUrl(baseUrl)
    }

    override suspend fun getBaseUrl(): BaseUrlModelDto {
        return remoteParser.getBaseUrl()
    }

    override suspend fun getStreamsBySeasonId(
        translationId: String,
        videoId: String,
        season: String,
        episode: String
    ): List<StreamDto> {
        return if (dataStorePrefs.getRemoteParsing().first())
            remoteParser.getStreamsBySeasonId(translationId, videoId, season, episode)
        else
            localParser.getStreamsBySeasonId(translationId, videoId, season, episode)
    }

    override suspend fun getStreamsByTranslatorId(translatorId: String): List<StreamDto> {
        return if (dataStorePrefs.getRemoteParsing().first())
            remoteParser.getStreamsByTranslatorId(translatorId)
        else
            localParser.getStreamsByTranslationId(translatorId)
    }
}