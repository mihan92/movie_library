package com.mihan.movie.library.data.repository

import com.mihan.movie.library.data.local.db.VideoHistoryDao
import com.mihan.movie.library.data.models.VideoHistoryDbModel
import com.mihan.movie.library.domain.LocalVideoHistoryRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class LocalVideoVideoHistoryRepositoryImpl @Inject constructor(
    private val videoHistoryDao: VideoHistoryDao
): LocalVideoHistoryRepository {
    override fun getVideoHistoryList(): Flow<List<VideoHistoryDbModel>> {
        return videoHistoryDao.getVideoHistoryList()
    }

    override fun getVideoHistoryById(videoId: String): Flow<VideoHistoryDbModel?> {
         return videoHistoryDao.getVideoHistoryById(videoId)
    }

    override suspend fun updateVideoHistory(videoHistoryDbModel: VideoHistoryDbModel) {
        videoHistoryDao.updateVideoHistory(videoHistoryDbModel)
    }

    override suspend fun deleteVideoHistoryById(videoId: String) {
        videoHistoryDao.deleteVideoHistoryById(videoId)
    }
}