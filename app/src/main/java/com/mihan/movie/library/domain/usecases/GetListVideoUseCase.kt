package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.entites.Filter
import com.mihan.movie.library.common.entites.VideoCategory
import com.mihan.movie.library.common.extentions.logger
import com.mihan.movie.library.data.models.toVideoItemModel
import com.mihan.movie.library.domain.ParserRepository
import com.mihan.movie.library.domain.models.VideoItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetListVideoUseCase @Inject constructor(private val parserRepository: ParserRepository) {
    suspend operator fun invoke(
        filter: Filter,
        videoCategory: VideoCategory,
        page: Int,
        baseUrl: String
    ): Flow<DtoState<List<VideoItemModel>>> = flow {
        try {
            emit(DtoState.Loading())
            val listOfMovies = parserRepository.getListVideo(filter, videoCategory, page, baseUrl).map { it.toVideoItemModel() }
            emit(DtoState.Success(listOfMovies))
        } catch (e: Exception) {
            logger(e.message.toString())
            emit(DtoState.Error(e.message ?: "GetDataUseCase Error"))
        }
    }
}