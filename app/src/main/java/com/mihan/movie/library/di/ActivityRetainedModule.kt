package com.mihan.movie.library.di

import android.content.Context
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.mihan.movie.library.data.repository.AppUpdateRepositoryImpl
import com.mihan.movie.library.data.repository.ParserRepositoryImpl
import com.mihan.movie.library.domain.AppUpdateRepository
import com.mihan.movie.library.domain.ParserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@[Module InstallIn(ActivityRetainedComponent::class)]
interface ActivityRetainedModule {

    @Binds
    fun bindsParserRepository(impl: ParserRepositoryImpl): ParserRepository

    @Binds
    fun bindsAppUpdateRepository(impl: AppUpdateRepositoryImpl): AppUpdateRepository

    companion object {
        @[Provides ActivityRetainedScoped]
        fun provideNavController(
            @ApplicationContext context: Context
        ): NavHostController = NavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(DialogNavigator())
        }
    }
}