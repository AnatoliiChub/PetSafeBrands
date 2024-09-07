package com.chub.petsafebrands.domain.pojo

sealed class UiResult<out T> {
    data class Success<T>(val data: T?) : UiResult<T>()
    data class Failure(val errorMessage: String) : UiResult<Nothing>()
}
