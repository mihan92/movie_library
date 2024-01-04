package com.mihan.movie.library.domain

import com.mihan.movie.library.data.models.ChangelogDto

interface AppUpdateRepository {
    suspend fun checkUpdates(): ChangelogDto
}