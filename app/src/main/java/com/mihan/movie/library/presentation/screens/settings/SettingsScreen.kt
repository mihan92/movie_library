package com.mihan.movie.library.presentation.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalTvMaterial3Api::class)
@Destination
@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val focusRequester = remember { FocusRequester() }
        Column {
            Text(text = "Settings screen")
            Button(
                modifier = Modifier.focusRequester(focusRequester),
                onClick = { }
            ) {
                Text(text = "Button 1")
            }
            Button(onClick = { }) {
                Text(text = "Button 2")
            }
            Button(onClick = { }) {
                Text(text = "Button 3")
            }
        }
//        LaunchedEffect(Unit) {
//            focusRequester.requestFocus()
//        }
    }
}