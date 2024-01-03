package com.mihan.movie.library.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.Surface
import androidx.tv.material3.rememberDrawerState
import com.mihan.movie.library.presentation.navigation.Screens
import com.mihan.movie.library.presentation.screens.NavGraphs
import com.mihan.movie.library.presentation.ui.theme.MovieLibraryTheme
import com.mihan.movie.library.presentation.ui.view.DrawerContent
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    internal lateinit var navController: NavHostController

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieLibraryTheme {
                val navController by remember { derivedStateOf { this.navController } }
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination by remember { derivedStateOf { navBackStackEntry?.destination?.route } }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            AnimatedVisibility(
                                visible = currentDestination in screensWithDrawer,
                                enter = slideInHorizontally(
                                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                                ) + fadeIn(),
                                exit = slideOutHorizontally(
                                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                                ) + fadeOut()
                            ) {
                                DrawerContent(
                                    drawerState = drawerState,
                                    currentDestination = currentDestination,
                                    navController = navController
                                )
                            }
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    ) {
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    companion object {
        private val screensWithDrawer = listOf(
            Screens.Home.route,
            Screens.Search.route,
            Screens.Settings.route,
            Screens.Placeholder.route
        )
    }
}


