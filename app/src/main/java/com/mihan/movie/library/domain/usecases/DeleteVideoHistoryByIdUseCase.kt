package com.mihan.movie.library.domain.usecases

import com.mihan.movie.library.domain.LocalVideoHistoryRepository
import javax.inject.Inject

class DeleteVideoHistoryByIdUseCase @Inject constructor(private val repository: LocalVideoHistoryRepository) {

    suspend operator fun invoke(filmId: String) {
        repository.deleteVideoHistoryById(filmId)
    }
}