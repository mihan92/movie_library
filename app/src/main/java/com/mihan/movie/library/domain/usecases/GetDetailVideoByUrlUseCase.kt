package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.extentions.logger
import com.mihan.movie.library.data.models.toVideoDetail
import com.mihan.movie.library.domain.ParserRepository
import com.mihan.movie.library.domain.models.VideoDetailModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDetailVideoByUrlUseCase @Inject constructor(private val parserRepository: ParserRepository) {
    suspend operator fun invoke(url: String): Flow<DtoState<VideoDetailModel>> = flow {
        try {
            emit(DtoState.Loading())
            val detailVideo = parserRepository.getDetailVideoByUrl(url).toVideoDetail()
            emit(DtoState.Success(detailVideo))
        } catch (e: Exception) {
            logger(e.message.toString())
            emit(DtoState.Error(e.message ?: "GetDetailVideoByUrlUseCase Error"))
        }
    }
}