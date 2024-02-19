package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.common.extentions.logger
import com.mihan.movie.library.domain.ParserRepository
import javax.inject.Inject

class UpdateBaseUrlUseCase @Inject constructor(private val parserRepository: ParserRepository) {

    suspend operator fun invoke(baseUrl: String) {
        try {
            parserRepository.updateBaseUrl(baseUrl)
        } catch (e: Exception) {
            logger("UpdateBaseUrlUseCase error ${e.message.toString()}")
        }
    }
}