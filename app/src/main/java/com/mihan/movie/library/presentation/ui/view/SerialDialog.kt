package com.mihan.movie.library.presentation.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.mihan.movie.library.presentation.ui.size10dp
import com.mihan.movie.library.presentation.ui.size28dp

private const val DIALOG_FRACTION = 0.8f
private const val DIALOG_RATIO = 3 / 2f
private const val DROP_DOWN_MENU_FRACTION = 0.365f

@OptIn(ExperimentalTvMaterial3Api::class)
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
                    .fillMaxSize(DIALOG_FRACTION)
                    .aspectRatio(DIALOG_RATIO)
                    .background(Color.DarkGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (translations.isNotEmpty())
                    ExpandableTranslationsSection(
                        listOfTranslations = translations.keys.toList(),
                        onTranslationItemClicked = onTranslationItemClicked,
                        modifier = modifier,
                    )
                LazyColumn {
                    items(seasons.keys.toList()) {season ->
                        var isExpanded by rememberSaveable { mutableStateOf(false) }
                        ExpandableSectionTitle(
                            title = "Сезон $season",
                            isExpanded = isExpanded,
                            modifier = Modifier.clickable { isExpanded = true }
                        )
                        DropdownMenu(
                            modifier = modifier
                                .fillMaxWidth(DROP_DOWN_MENU_FRACTION)
                                .background(Color.DarkGray),
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }
                        ) {
                            seasons.getValue(season).forEach { episode ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "Серия $episode",
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    },
                                    onClick = {
                                        onEpisodeClicked(season, episode)
                                        isExpanded = false
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ExpandableTranslationsSection(
    listOfTranslations: List<String>,
    onTranslationItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedElement by rememberSaveable { mutableStateOf(listOfTranslations.first()) }
    Column(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .background(color = Color.DarkGray)
    ) {
        ExpandableSectionTitle(isExpanded = isExpanded, title = selectedElement)
        DropdownMenu(
            modifier = modifier
                .fillMaxWidth(DROP_DOWN_MENU_FRACTION)
                .background(Color.DarkGray),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            listOfTranslations.forEach { translator ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = translator,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    onClick = {
                        selectedElement = translator
                        onTranslationItemClicked(selectedElement)
                        isExpanded = false
                    })
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun ExpandableSectionTitle(
    title: String,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val icon = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(size10dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground
        )
        Image(
            modifier = Modifier.size(size28dp),
            imageVector = icon,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
            contentDescription = null
        )
    }
}