package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.extentions.logger
import com.mihan.movie.library.data.models.toVideoModel
import com.mihan.movie.library.domain.ParserRepository
import com.mihan.movie.library.domain.models.VideoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTranslationsByUrlUseCase @Inject constructor(private val parserRepository: ParserRepository) {
    suspend operator fun invoke(filmUrl: String): Flow<DtoState<VideoModel>> = flow {
        try {
            emit(DtoState.Loading())
            val translations = parserRepository.getTranslationsByUrl(filmUrl).toVideoModel()
            emit(DtoState.Success(translations))
        } catch (e: Exception) {
            logger(e.message.toString())
            emit(DtoState.Error(e.message ?: "GetTranslationsUseCase Error"))
        }
    }
}