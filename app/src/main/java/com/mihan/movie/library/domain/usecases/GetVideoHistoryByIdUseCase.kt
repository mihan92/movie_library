package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.data.models.toVideoHistoryModel
import com.mihan.movie.library.domain.LocalVideoHistoryRepository
import com.mihan.movie.library.domain.models.VideoHistoryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetVideoHistoryByIdUseCase @Inject constructor(private val repository: LocalVideoHistoryRepository) {

    operator fun invoke(filmId: String): Flow<VideoHistoryModel?> {
        return repository.getVideoHistoryById(filmId).map { it?.toVideoHistoryModel() }
    }
}