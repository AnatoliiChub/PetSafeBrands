package com.chub.petsafebrands.ui.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun TextFloatingButton(@StringRes text: Int, icon: ImageVector, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        modifier = Modifier.imePadding(),
        icon = { Icon(icon, null) },
        text = { Text(text = stringResource(text)) },
        onClick = onClick,
    )
}