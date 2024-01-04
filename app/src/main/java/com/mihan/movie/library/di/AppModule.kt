package com.mihan.movie.library.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mihan.movie.library.common.Constants
import com.mihan.movie.library.data.remote.GsonApiService
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
        fun provideGsonApiService(retrofit: Retrofit.Builder) = retrofit
            .baseUrl(Constants.GSON_API_BASE_URL)
            .build()
            .create(GsonApiService::class.java)

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
        fun provideGson() = GsonBuilder()
            .setLenient()
            .create()

        @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient, gson: Gson) = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
    }
}