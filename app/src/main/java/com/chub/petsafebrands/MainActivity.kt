package com.chub.petsafebrands

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.chub.petsafebrands.navigation.AppNavigation
import com.chub.petsafebrands.ui.theme.PetSafeBrandsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetSafeBrandsTheme {
                AppNavigation()
            }
        }
    }
}