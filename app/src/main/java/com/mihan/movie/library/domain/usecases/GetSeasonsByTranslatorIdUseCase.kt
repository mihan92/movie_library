package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.extentions.logger
import com.mihan.movie.library.data.models.toSeasonModel
import com.mihan.movie.library.domain.ParserRepository
import com.mihan.movie.library.domain.models.SeasonModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSeasonsByTranslatorIdUseCase @Inject constructor(private val parserRepository: ParserRepository) {

    suspend operator fun invoke(translatorId: String): Flow<DtoState<List<SeasonModel>>> = flow {
        emit(DtoState.Loading())
        try {
            val seasonList = parserRepository.getSeasonsByTranslatorId(translatorId).map { it.toSeasonModel() }
            emit(DtoState.Success(seasonList))
        } catch (e: Exception) {
            logger(e.message.toString())
            emit(DtoState.Error(e.message ?: "GetSeasonsByTranslatorIdUseCase Error"))
        }
    }
}