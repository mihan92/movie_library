package com.mihan.movie.library.presentation.screens.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.items
import androidx.tv.foundation.lazy.grid.rememberTvLazyGridState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import coil.compose.SubcomposeAsyncImage
import com.mihan.movie.library.common.entites.VideoCategory
import com.mihan.movie.library.domain.models.VideoItemModel
import com.mihan.movie.library.presentation.animation.AnimatedScreenTransitions
import com.mihan.movie.library.presentation.screens.destinations.DetailVideoScreenDestination
import com.mihan.movie.library.presentation.ui.size10dp
import com.mihan.movie.library.presentation.ui.size16sp
import com.mihan.movie.library.presentation.ui.size28dp
import com.mihan.movie.library.presentation.ui.size2dp
import com.mihan.movie.library.presentation.ui.size4dp
import com.mihan.movie.library.presentation.ui.size8dp
import com.mihan.movie.library.presentation.ui.theme.primaryColor
import com.mihan.movie.library.presentation.ui.view.PageFooter
import com.mihan.movie.library.presentation.ui.view.TopAppBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private const val numberOfGridCells = 6

@OptIn(ExperimentalTvMaterial3Api::class)
@Destination(style = AnimatedScreenTransitions::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val currentPage by viewModel.pageState.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            currentFilterSection = filterState,
            onItemClick = viewModel::onTopBarItemClicked
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (screenState.isLoading)
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            if (screenState.errorMessage.isNotEmpty())
                Toast.makeText(LocalContext.current, screenState.errorMessage, Toast.LENGTH_LONG).show()
            if (screenState.data.isNotEmpty()) {
                var page = currentPage
                Content(
                    listOfVideos = screenState.data,
                    navigator = navigator,
                    currentPage = currentPage,
                    previousPageClick = { viewModel.onPageChanged(--page) },
                    nextPageClick = { viewModel.onPageChanged(++page) }
                )
            }
        }
    }
}

@Composable
private fun Content(
    listOfVideos: List<VideoItemModel>,
    navigator: DestinationsNavigator,
    currentPage: Int,
    previousPageClick: () -> Unit,
    nextPageClick: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.padding(bottom = size10dp)
    ) {
        val state = rememberTvLazyGridState()
        TvLazyVerticalGrid(
            state = state,
            columns = TvGridCells.Fixed(numberOfGridCells),
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
        ) {
            items(listOfVideos) { item ->
                MovieItem(
                    title = item.title,
                    category = item.category,
                    imageUrl = item.imageUrl,
                    onItemClick = { navigator.navigate(DetailVideoScreenDestination(item.videoUrl)) },
                )
            }
        }
        AnimatedVisibility(
            visible = !state.canScrollForward,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            PageFooter(
                currentPage = currentPage,
                listSize = listOfVideos.size,
                previousPageClick = previousPageClick,
                nextPageClick = nextPageClick
            )
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieItem(
    title: String,
    category: String,
    imageUrl: String,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var borderColor by remember { mutableStateOf(Color.Transparent) }
    Card(
        modifier = modifier
            .aspectRatio(2 / 3f)
            .padding(size10dp)
            .onFocusChanged {
                borderColor = if (it.isFocused) primaryColor else Color.Transparent
            },
        onClick = onItemClick,
        shape = RoundedCornerShape(size8dp),
        border = BorderStroke(size2dp, borderColor)
    ) {
        Box(
            modifier = Modifier.background(Color.DarkGray),
            contentAlignment = Alignment.TopEnd
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                SubcomposeAsyncImage(
                    model = imageUrl,
                    loading = { CircularProgressIndicator() },
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(size4dp)
                        .clip(RoundedCornerShape(size8dp))
                        .weight(1f),
                )
                VideoTitle(title = title)
            }
            if (category.isNotEmpty())
                Category(category)
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun VideoTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(size28dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = size16sp,
            fontWeight = FontWeight.W700,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = modifier
                .padding(horizontal = size2dp)
                .horizontalScroll(rememberScrollState()),
        )
    }

}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun Category(
    filmCategory: String
) {
    Box(
        modifier = Modifier
            .background(VideoCategory.getColorFromCategory(filmCategory), RoundedCornerShape(size4dp))
            .padding(size4dp)
    ) {
        Text(
            text = filmCategory,
            fontWeight = FontWeight.W700,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}