package com.mihan.movie.library.presentation.screens.app_updates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.mihan.movie.library.R
import com.mihan.movie.library.presentation.animation.AnimatedScreenTransitions
import com.mihan.movie.library.presentation.ui.size18sp
import com.mihan.movie.library.presentation.ui.size28dp
import com.mihan.movie.library.presentation.ui.size6dp
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalTvMaterial3Api::class)
@Destination(style = AnimatedScreenTransitions::class)
@Composable
fun AppUpdatesScreen(
    appUpdatesViewModel: AppUpdatesViewModel = hiltViewModel()
) {
    val changelogModel = appUpdatesViewModel.changelogModel
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        changelogModel?.let { changelog ->
            Text(
                text = stringResource(id = R.string.new_version_is_available, changelog.latestVersion),
                fontSize = size18sp,
                fontWeight = FontWeight.W900
            )
            Spacer(modifier = Modifier.height(size28dp))
            ReleaseNotes(listOfNotes = changelog.releaseNotes)
            Spacer(modifier = Modifier.height(size28dp))
            Button(
                onClick = appUpdatesViewModel::buttonUpdatePressed,
                colors = ButtonDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(text = stringResource(id = R.string.update_title))
            }
        }
    }
}

@Composable
private fun ReleaseNotes(
    listOfNotes: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(listOfNotes) {
            Text(
                text = it,
                fontSize = size18sp,
                modifier = modifier.padding(vertical = size6dp)
            )
        }
    }
}