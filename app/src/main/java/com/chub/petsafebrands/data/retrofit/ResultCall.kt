package com.chub.petsafebrands.data.retrofit

import com.chub.petsafebrands.data.response.ErrorResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class ResultCall<T>(proxy: Call<T>, private val gson: Gson) : CallDelegate<T, Result<T>>(proxy) {
    override fun enqueueImpl(callback: Callback<Result<T>>) = proxy.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val code = response.code()
            val result = if (code in 200 until 300) {
                val body = response.body()
                Result.Success(body)
            } else {
                response.errorBody()?.use {
                    val error = gson.fromJson(it.string(), ErrorResponse::class.java).error.info
                    Result.Failure(code, error)
                }
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val result = when (t) {
                is SocketTimeoutException -> Result.Failure(null, "Timeout error")
                is IOException -> Result.NetworkError
                else -> Result.Failure(null, t.message ?: "Unknown error")
            }
            callback.onResponse(this@ResultCall, Response.success(result))
        }
    })

    override fun cloneImpl() = ResultCall(proxy.clone(), gson)
}
