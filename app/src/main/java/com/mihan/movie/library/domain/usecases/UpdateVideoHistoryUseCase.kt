package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.domain.LocalVideoHistoryRepository
import com.mihan.movie.library.domain.models.VideoHistoryModel
import com.mihan.movie.library.domain.models.toVideoHistoryDbModel
import javax.inject.Inject

class UpdateVideoHistoryUseCase @Inject constructor(private val repository: LocalVideoHistoryRepository) {

    suspend operator fun invoke(videoHistoryModel: VideoHistoryModel) {
        repository.updateVideoHistory(videoHistoryModel.toVideoHistoryDbModel())
    }
}