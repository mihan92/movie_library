package com.mihan.movie.library.data.remote

import com.mihan.movie.library.data.models.ChangelogDto
import retrofit2.http.GET

interface GsonApiService {
    @GET("/v/movielibrary")
    suspend fun checkUpdates(): ChangelogDto
}