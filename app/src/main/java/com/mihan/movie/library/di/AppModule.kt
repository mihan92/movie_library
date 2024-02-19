package com.mihan.movie.library.di

import android.app.DownloadManager
import android.content.Context
import com.mihan.movie.library.common.Constants
import com.mihan.movie.library.common.utils.DownloadManagerImpl
import com.mihan.movie.library.common.utils.IDownloadManager
import com.mihan.movie.library.data.remote.GsonApiService
import com.mihan.movie.library.data.remote.RemoteParserApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun provideIDownloadManager(impl: DownloadManagerImpl): IDownloadManager

    companion object {

        @Provides
        @Singleton
        fun provideSystemDownloadManager(@ApplicationContext context: Context): DownloadManager =
            context.getSystemService(DownloadManager::class.java)

        @Provides
        @Singleton
        fun provideRemoteParserApiService(retrofit: Retrofit.Builder): RemoteParserApiService = retrofit
            .baseUrl(Constants.REMOTE_PARSER_API_BASE_URL)
            .build()
            .create(RemoteParserApiService::class.java)

        @Provides
        @Singleton
        fun provideGsonApiService(retrofit: Retrofit.Builder): GsonApiService = retrofit
            .baseUrl(Constants.GSON_API_BASE_URL)
            .build()
            .create(GsonApiService::class.java)

        @Provides
        @Singleton
        fun provideInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)

        @Provides
        @Singleton
        fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
    }
}