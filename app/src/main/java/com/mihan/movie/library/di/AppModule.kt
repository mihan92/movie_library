package com.mihan.movie.library.di

import com.mihan.movie.library.common.Constants
import com.mihan.movie.library.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    companion object {

        @Provides
        @Singleton
        fun provideInterseptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)


        @Provides
        @Singleton
        fun provideOkHttpClient(interseptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interseptor)
            .build()

        @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient) = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}