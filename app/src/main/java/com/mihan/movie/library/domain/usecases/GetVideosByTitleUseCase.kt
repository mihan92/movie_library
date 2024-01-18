package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.extentions.logger
import com.mihan.movie.library.data.models.toVideoItemModel
import com.mihan.movie.library.domain.ParserRepository
import com.mihan.movie.library.domain.models.VideoItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetVideosByTitleUseCase @Inject constructor(private val parserRepository: ParserRepository) {

    suspend operator fun invoke(videoTitle: String): Flow<DtoState<List<VideoItemModel>>> = flow {
        try {
            emit(DtoState.Loading())
            val listOfVideo = parserRepository.getVideosByTitle(videoTitle).map { it.toVideoItemModel() }
            emit(DtoState.Success(listOfVideo))
        } catch (e: Exception) {
            logger(e.message.toString())
            emit(DtoState.Error(e.message ?: "GetVideosByTitleUseCase Error"))
        }
    }
}