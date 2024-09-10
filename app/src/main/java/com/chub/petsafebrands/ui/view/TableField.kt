package com.chub.petsafebrands.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.TableField(text: String, isSelected: Boolean = false, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .weight(1f)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 4.dp),
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
        if (isSelected) {
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
        }
        Spacer(modifier = Modifier.weight(1f))
    }

}

@Preview
@Composable
fun TableFieldPreview() {
    Row { TableField(text = "Date", false) {} }
}