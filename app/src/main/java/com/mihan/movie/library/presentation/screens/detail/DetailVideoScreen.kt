package com.mihan.movie.library.presentation.screens.detail

import android.widget.Toast
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.SubcomposeAsyncImage
import com.mihan.movie.library.R
import com.mihan.movie.library.domain.models.VideoDetailModel
import com.mihan.movie.library.presentation.animation.AnimatedScreenTransitions
import com.mihan.movie.library.presentation.ui.size16dp
import com.mihan.movie.library.presentation.ui.size18sp
import com.mihan.movie.library.presentation.ui.size200dp
import com.mihan.movie.library.presentation.ui.size300dp
import com.mihan.movie.library.presentation.ui.size32sp
import com.mihan.movie.library.presentation.ui.size4dp
import com.mihan.movie.library.presentation.ui.size50dp
import com.mihan.movie.library.presentation.ui.view.FilmDialog
import com.mihan.movie.library.presentation.ui.view.SerialDialog
import com.ramcosta.composedestinations.annotation.Destination


@OptIn(ExperimentalTvMaterial3Api::class)
@Destination(navArgsDelegate = DetailScreenNavArgs::class, style = AnimatedScreenTransitions::class)
@Composable
fun DetailVideoScreen(
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    val screenState by detailViewModel.screenState.collectAsStateWithLifecycle()
    val filmDialogState = detailViewModel.showFilmDialog.collectAsStateWithLifecycle()
    val serialDialogState = detailViewModel.showSerialDialog.collectAsStateWithLifecycle()
    val movieStreamsState by detailViewModel.listofStreams.collectAsStateWithLifecycle()
    val dataState by detailViewModel.videoData.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (screenState.isLoading)
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        if (screenState.errorMessage.isNotEmpty())
            Toast.makeText(LocalContext.current, screenState.errorMessage, Toast.LENGTH_LONG).show()
        screenState.detailInfo?.let { detailModel ->
            Content(
                videoDetailModel = detailModel,
                onButtonWatchClick = { detailViewModel.getTranslations() }
            )
        }
        movieStreamsState?.let { list ->
            detailViewModel.sendIntent(list, context)
        }
        FilmDialog(
            isDialogShow = filmDialogState,
            translations = dataState.translations,
            onTranslationItemClicked = { translate ->
                val selectedTranslate = dataState.videoStreamsWithTranslatorName.getValue(translate)
                detailViewModel.selectedTranslate(selectedTranslate)
            },
            onDialogDismiss = detailViewModel::onDialogDismiss
        )
        SerialDialog(
            isDialogShow = serialDialogState,
            translations = dataState.translations,
            seasons = dataState.seasonList,
            onTranslationItemClicked = { translate ->
                val translatorId = dataState.translations.getValue(translate)
                detailViewModel.selectedTranslatorId(translatorId)
            },
            onEpisodeClicked = { season, episode ->
                detailViewModel.onEpisodeClicked(season, episode)
            },
            onDialogDismiss = detailViewModel::onDialogDismiss
        )
    }
}

@Composable
private fun Content(
    videoDetailModel: VideoDetailModel,
    onButtonWatchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = size50dp)
            .padding(horizontal = size50dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VideoTitle(
            videoDetailModel = videoDetailModel,
            focusRequester = focusRequester
        )
        Row {
            Poster(videoDetailModel)
            VideoInfo(
                videoDetailModel = videoDetailModel,
                focusRequester = focusRequester,
                onButtonWatchClick = onButtonWatchClick
            )
        }
        Text(
            text = videoDetailModel.description,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .focusable()
                .focusRequester(focusRequester)
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun VideoTitle(
    videoDetailModel: VideoDetailModel,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .focusable(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = videoDetailModel.title,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = size32sp,
            modifier = modifier.padding(bottom = size16dp)
        )
    }
}

@Composable
private fun Poster(
    videoDetailModel: VideoDetailModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(size300dp)
            .width(size200dp)
    ) {
        SubcomposeAsyncImage(
            model = videoDetailModel.imageUrl,
            loading = { CircularProgressIndicator() },
            contentDescription = videoDetailModel.title,
            contentScale = ContentScale.FillBounds,
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun VideoInfo(
    videoDetailModel: VideoDetailModel,
    focusRequester: FocusRequester,
    onButtonWatchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        ItemInfo(title = videoDetailModel.ratingIMdb)
        ItemInfo(title = stringResource(id = R.string.rating_hdr_title, videoDetailModel.ratingHdrezka))
        ItemInfo(title = videoDetailModel.releaseDate)
        ItemInfo(title = videoDetailModel.country)
        ItemInfo(title = videoDetailModel.genre)
        Button(
            shape = RoundedCornerShape(size4dp),
            onClick = onButtonWatchClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(top = size16dp)
        ) {
            Text(text = stringResource(id = R.string.bt_watch))
        }
    }
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun ItemInfo(
    title: String
) {
    Column {
        Text(
            text = title,
            fontSize = size18sp
        )
        Spacer(modifier = Modifier.height(size16dp))
    }
}
