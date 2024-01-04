package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.data.models.toChangelogModel
import com.mihan.movie.library.domain.AppUpdateRepository
import com.mihan.movie.library.domain.models.ChangelogModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckUpdatesUseCase @Inject constructor(private val repository: AppUpdateRepository) {
    suspend operator fun invoke(): Flow<DtoState<ChangelogModel>> = flow {
        try {
            emit(DtoState.Loading())
            val updates = repository.checkUpdates().toChangelogModel()
            emit(DtoState.Success(updates))
        } catch (e: Exception) {
            emit(DtoState.Error(e.message ?: "CheckUpdatesUseCase Error"))
        }
    }
}