package com.mihan.hdrezka.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.Surface
import androidx.tv.material3.rememberDrawerState
import com.mihan.hdrezka.presentation.navigation.Screens
import com.mihan.hdrezka.presentation.screens.NavGraphs
import com.mihan.hdrezka.presentation.ui.theme.HDrezkaTheme
import com.mihan.hdrezka.presentation.ui.view.DrawerContent
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
            HDrezkaTheme {
                val navController by remember { derivedStateOf { this.navController } }
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination?.route
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            if (currentDestination in screensWithDrawer)
                                DrawerContent(
                                    drawerValue = drawerState.currentValue,
                                    currentDestination = currentDestination,
                                    navController = navController
                                )
                        },
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
            Screens.Settings.route
        )
    }
}


