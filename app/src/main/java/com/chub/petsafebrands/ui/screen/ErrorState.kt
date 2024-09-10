package com.chub.petsafebrands.ui.screen

sealed class ErrorState {
    data object None : ErrorState()
    data class Error(val message: String) : ErrorState()
}