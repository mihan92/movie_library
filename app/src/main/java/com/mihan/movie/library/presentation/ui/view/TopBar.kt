package com.mihan.movie.library.presentation.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.mihan.movie.library.common.entites.Filter
import com.mihan.movie.library.presentation.ui.size14sp
import com.mihan.movie.library.presentation.ui.size16sp
import com.mihan.movie.library.presentation.ui.size60dp


@Composable
fun TopAppBar(
    currentFilterSection: Filter,
    onItemClick: (Filter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(size60dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Filter.entries.forEach { item ->
            FilterItem(
                title = stringResource(id = item.titleResId),
                selected = item.section == currentFilterSection.section,
                onItemClick = { onItemClick(item) }
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun FilterItem(
    title: String,
    selected: Boolean,
    onItemClick: () -> Unit
) {
    val textColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
    Button(
        onClick = onItemClick,
        colors = ButtonDefaults.colors(
            containerColor = Color.Transparent,
            focusedContainerColor = Color.White.copy(0.1f),
        )
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.W700,
            fontSize = if (selected) size16sp else size14sp,
            color = textColor
        )
    }
}