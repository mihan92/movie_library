package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.extentions.logger
import com.mihan.movie.library.data.models.toStreamModel
import com.mihan.movie.library.domain.ParserRepository
import com.mihan.movie.library.domain.models.StreamModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStreamsBySeasonIdUseCase @Inject constructor(private val parserRepository: ParserRepository) {
    suspend operator fun invoke(
        translationId: String,
        videoId: String,
        season: String,
        episode: String
    ): Flow<DtoState<List<StreamModel>>> = flow {
        try {
            emit(DtoState.Loading())
            val streams = parserRepository
                .getStreamsBySeasonId(translationId, videoId, season, episode)
                .map { it.toStreamModel() }
            emit(DtoState.Success(streams))
        } catch (e: Exception) {
            logger(e.message.toString())
            emit(DtoState.Error(e.message ?: "GetStreamsBySeasonIdUseCase Error"))
        }
    }
}