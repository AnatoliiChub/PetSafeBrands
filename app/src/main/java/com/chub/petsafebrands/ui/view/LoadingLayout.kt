package com.chub.petsafebrands.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingLayout() {
    Box(modifier = Modifier
        .clickable(enabled = false) {}
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.2f))) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(128.dp)
                .align(Alignment.Center)
        )
    }
}