package com.mihan.movie.library.presentation.ui.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.mihan.movie.library.presentation.ui.size10dp
import com.mihan.movie.library.presentation.ui.size16dp
import com.mihan.movie.library.presentation.ui.size16sp
import com.mihan.movie.library.presentation.ui.size1dp

private const val HEADER_FRACTION = 0.8f

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DropDownHeader(
    selectedItemTitle: String,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(HEADER_FRACTION)
            .border(size1dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(size10dp))
            .padding(vertical = size10dp)
            .padding(horizontal = size16dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = selectedItemTitle,
            fontSize = size16sp,
            fontWeight = FontWeight.W600,
            color = MaterialTheme.colorScheme.onBackground
        )
        Icon(
            imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = null
        )
    }
}