package com.mihan.movie.library.data.remote

import com.mihan.movie.library.BuildConfig
import com.mihan.movie.library.data.models.BaseUrlModelDto
import com.mihan.movie.library.data.models.SeasonModelDto
import com.mihan.movie.library.data.models.StreamDto
import com.mihan.movie.library.data.models.VideoDetailDto
import com.mihan.movie.library.data.models.VideoDto
import com.mihan.movie.library.data.models.VideoItemDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteParserApiService {

    @Headers("Authorization: ${BuildConfig.ACCESS_TOKEN}")
    @GET("api/listVideo")
    suspend fun getListVideo(
        @Query("filter") filter: String,
        @Query("category") category: String,
        @Query("page") page: Int
    ): List<VideoItemDto>

    @Headers("Authorization: ${BuildConfig.ACCESS_TOKEN}")
    @GET("api/detailVideoInfo")
    suspend fun getDetailVideoByUrl(
        @Query("url") url: String
    ): VideoDetailDto

    @Headers("Authorization: ${BuildConfig.ACCESS_TOKEN}")
    @GET("api/translations")
    suspend fun getTranslationsByUrl(
        @Query("url") url: String
    ): VideoDto

    @Headers("Authorization: ${BuildConfig.ACCESS_TOKEN}")
    @GET("api/listStreamBySeasonId")
    suspend fun getStreamsBySeasonId(
        @Query("translatorId") translationId: String,
        @Query("videoId") videoId: String,
        @Query("season") season: String,
        @Query("episode") episode: String
    ): List<StreamDto>

    @Headers("Authorization: ${BuildConfig.ACCESS_TOKEN}")
    @GET("api/listVideoByTitle")
    suspend fun getVideosByTitle(
        @Query("videoTitle") videoTitle: String
    ): List<VideoItemDto>

    @Headers("Authorization: ${BuildConfig.ACCESS_TOKEN}")
    @GET("api/seasonListById")
    suspend fun getSeasonsByTranslatorId(
        @Query("translatorId") translatorId: String
    ): List<SeasonModelDto>

    @Headers("Authorization: ${BuildConfig.ACCESS_TOKEN}")
    @POST("api/updateBaseUrl")
    suspend fun updateBaseUrl(
        @Query("baseUrl") baseUrl: String
    )

    @Headers("Authorization: ${BuildConfig.ACCESS_TOKEN}")
    @GET("api/getBaseUrl")
    suspend fun getBaseUrl(): BaseUrlModelDto

    @Headers("Authorization: ${BuildConfig.ACCESS_TOKEN}")
    @GET("api/listStreamByTranslatorId")
    suspend fun getStreamsByTranslatorId(
        @Query("translatorId") translatorId: String
    ): List<StreamDto>
}