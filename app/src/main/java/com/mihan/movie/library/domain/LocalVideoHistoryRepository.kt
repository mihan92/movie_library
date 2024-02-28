package com.mihan.movie.library.domain

import com.mihan.movie.library.data.models.VideoHistoryDbModel
import kotlinx.coroutines.flow.Flow

interface LocalVideoHistoryRepository {

    fun getVideoHistoryList(): Flow<List<VideoHistoryDbModel>>

    fun getVideoHistoryById(videoId: String): Flow<VideoHistoryDbModel?>

    suspend fun updateVideoHistory(videoHistoryDbModel: VideoHistoryDbModel)

    suspend fun deleteVideoHistoryById(videoId: String)
}