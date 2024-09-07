package com.chub.petsafebrands.data.retrofit

sealed class Result<out T> {
    data class Success<T>(val data: T?) : Result<T>()
    data class Failure(val statusCode: Int?, val errorMessage: String) : Result<Nothing>()
    data object NetworkError : Result<Nothing>()
}