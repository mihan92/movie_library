package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.extentions.logger
import com.mihan.movie.library.data.models.toBaseUrlModel
import com.mihan.movie.library.domain.ParserRepository
import com.mihan.movie.library.domain.models.BaseUrlModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBaseUrlUseCase @Inject constructor(private val parserRepository: ParserRepository) {

    suspend operator fun invoke(): Flow<DtoState<BaseUrlModel>> = flow {
        try {
            val baseUrl = parserRepository.getBaseUrl().toBaseUrlModel()
            emit(DtoState.Success(baseUrl))
        } catch (e: Exception) {
            logger("GetBaseUrlUseCase Error ${e.message}")
            emit(DtoState.Error(e.message ?: "GetBaseUrlUseCase Error"))
        }
    }
}