package com.chub.petsafebrands.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RowScope.TableField(text: String) {
    Text(
        modifier = Modifier.weight(1f),
        text = text,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Preview
@Composable
fun TableFieldPreview() {
    Row { TableField(text = "Date") }
}