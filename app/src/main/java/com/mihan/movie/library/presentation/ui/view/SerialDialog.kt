package com.mihan.movie.library.presentation.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.Dialog
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyListScope
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.mihan.movie.library.R
import com.mihan.movie.library.presentation.ui.size10dp
import com.mihan.movie.library.presentation.ui.size14sp
import com.mihan.movie.library.presentation.ui.size16dp
import com.mihan.movie.library.presentation.ui.size16sp
import com.mihan.movie.library.presentation.ui.size28dp
import com.mihan.movie.library.presentation.ui.size8dp

private const val DIALOG_RATIO = 2 / 1.5f

@Composable
fun SerialDialog(
    isDialogShow: State<Boolean>,
    translations: Map<String, String>,
    seasons: Map<String, List<String>>,
    onTranslationItemClicked: (String) -> Unit,
    onEpisodeClicked: (String, String) -> Unit,
    onDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isDialogShow.value) {
        Dialog(onDismissRequest = onDialogDismiss) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .aspectRatio(DIALOG_RATIO)
                    .background(Color.DarkGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExpandableTranslationList(
                    translations = translations.keys.toList(),
                    onTranslationItemClicked = onTranslationItemClicked
                )
                ExpandableSeasonList(
                    sections = seasons,
                    onEpisodeClicked = onEpisodeClicked
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun ExpandableTranslationList(
    translations: List<String>,
    onTranslationItemClicked: (String) -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedElement by rememberSaveable { mutableStateOf(translations.first()) }
    Column {
        TextField(
            value = selectedElement,
            textStyle = TextStyle(fontSize = size16sp),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = stringResource(id = R.string.voicecover_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = size14sp
                )
            },
            trailingIcon = { ExpanableIcon(isExpanded = isExpanded) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
        )
        if (isExpanded) {
            TvLazyColumn {
                items(translations) { translateItem ->
                    DialogText(
                        title = translateItem,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedElement = translateItem
                                onTranslationItemClicked(translateItem)
                                isExpanded = false
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandableSeasonList(
    sections: Map<String, List<String>>,
    onEpisodeClicked: (String, String) -> Unit
) {
    val isExpandedMap = remember { List(sections.size) { index: Int -> index to false }.toMutableStateMap() }
    TvLazyColumn(
        content = {
            sections.onEachIndexed { index, value ->
                section(
                    header = value.key,
                    listData = value.value,
                    isExpanded = isExpandedMap[index] ?: true,
                    onHeaderClick = {
                        isExpandedMap[index] = !(isExpandedMap[index] ?: true)
                    },
                    onEpisodeClicked = { onEpisodeClicked(value.key, it) }
                )
            }
        }
    )
}

@Composable
private fun SectionHeader(
    seasonTitle: String,
    isExpanded: Boolean,
    onHeaderClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onHeaderClicked() }
            .background(Color.DarkGray)
            .padding(vertical = size8dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DialogText(title = stringResource(id = R.string.season_title, seasonTitle))
        ExpanableIcon(
            isExpanded = isExpanded,
            modifier = Modifier.padding(end = size10dp)
        )
    }
}

private fun TvLazyListScope.section(
    header: String,
    listData: List<String>,
    isExpanded: Boolean,
    onEpisodeClicked: (String) -> Unit,
    onHeaderClick: () -> Unit
) {
    item {
        SectionHeader(
            seasonTitle = header,
            isExpanded = isExpanded,
            onHeaderClicked = onHeaderClick
        )
    }
    if (isExpanded) {
        items(listData) {
            DialogText(
                title = stringResource(id = R.string.episode_title, it),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEpisodeClicked(it) }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalTvMaterial3Api::class)
private fun ExpanableIcon(
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    val icon = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown
    Image(
        modifier = modifier.size(size28dp),
        imageVector = icon,
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
        contentDescription = null
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun DialogText(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = size16sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
            .padding(vertical = size8dp, horizontal = size16dp)
            .focusable()
    )
}