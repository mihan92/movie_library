package com.mihan.movie.library.presentation.ui.view

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.DrawerState
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.mihan.movie.library.R
import com.mihan.movie.library.presentation.navigation.Screens
import com.mihan.movie.library.presentation.navigation.popUpToMain
import com.mihan.movie.library.presentation.ui.size18sp
import com.mihan.movie.library.presentation.ui.size4dp

private const val DRAWER_WIDTH_FRACTION = 0.12f
private const val CONTENT_ALPHA = 0.7f

enum class DrawerItems(val route: String, val icon: ImageVector, @DrawableRes val titleResId: Int) {
    Updates(Screens.AppUpdatesScreen.route, Icons.Rounded.Notifications, R.string.updates_route_title),
    Search(Screens.Search.route, Icons.Rounded.Search, R.string.search_route_title),
    Home(Screens.Home.route, Icons.Rounded.Home, R.string.home_route_title),
    Settings(Screens.Placeholder.route, Icons.Rounded.Settings, R.string.settings_route_title)
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DrawerContent(
    drawerState: DrawerState,
    currentDestination: String?,
    navController: NavHostController,
    isAppUpdatesAvailable: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background.copy(CONTENT_ALPHA))
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        DrawerItems.entries.forEach { item ->
            NavigationItem(
                drawerState = drawerState,
                icon = item.icon,
                titleResId = item.titleResId,
                selected = item.route == currentDestination,
                isVisible = if (item.route == Screens.AppUpdatesScreen.route) isAppUpdatesAvailable else true,
                onItemClick = {
                    navController.navigate(item.route) {
                        popUpToMain()
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun NavigationItem(
    drawerState: DrawerState,
    icon: ImageVector,
    titleResId: Int,
    selected: Boolean,
    isVisible: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var buttonBackgroundColor by remember { mutableStateOf(Color.Transparent) }
    if (isVisible)
        Button(
            modifier = modifier
                .padding(vertical = size4dp)
                .background(buttonBackgroundColor)
                .onFocusChanged { focusState ->
                    buttonBackgroundColor = if (focusState.isFocused) Color.White.copy(0.1f) else Color.Transparent
                },
            onClick = onItemClick,
            colors = ButtonDefaults.colors(
                containerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
            shape = ButtonDefaults.shape(RectangleShape),
        ) {
            val selectedContentColor =
                if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = size4dp),
                tint = selectedContentColor
            )
            AnimatedVisibility(visible = drawerState.currentValue == DrawerValue.Open) {
                Text(
                    text = stringResource(id = titleResId),
                    textAlign = TextAlign.Start,
                    color = selectedContentColor,
                    fontSize = size18sp,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier.fillMaxWidth(DRAWER_WIDTH_FRACTION)
                )
            }
        }
}