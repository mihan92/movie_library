package com.mihan.movie.library.data.repository

import com.mihan.movie.library.data.models.ChangelogDto
import com.mihan.movie.library.data.remote.GsonApiService
import com.mihan.movie.library.domain.AppUpdateRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
@ActivityRetainedScoped
class AppUpdateRepositoryImpl @Inject constructor(
    private val gsonApiService: GsonApiService
): AppUpdateRepository {
    override suspend fun checkUpdates(): ChangelogDto {
        return gsonApiService.checkUpdates()
    }
}