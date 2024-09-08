package com.chub.petsafebrands.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chub.petsafebrands.parcelableType
import com.chub.petsafebrands.ui.screen.rates.RatesScreen
import com.chub.petsafebrands.ui.screen.daily.DailyFxRatesScreen
import kotlin.reflect.typeOf

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = RatesScreenNav) {
        composable<RatesScreenNav> { RatesScreen({ navController.navigate(it) }) }
        composable<TimeSeriesScreenNav>(
            typeMap = mapOf(
                typeOf<TimeSeriesScreenNav>() to parcelableType<TimeSeriesScreenNav>(),
            )
        ) { DailyFxRatesScreen(onBack = { navController.popBackStack() }) }
    }
}

