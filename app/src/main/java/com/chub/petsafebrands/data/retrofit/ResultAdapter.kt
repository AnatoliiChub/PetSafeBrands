package com.chub.petsafebrands.data.retrofit

import com.google.gson.Gson
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ResultAdapter(
    private val type: Type,
    private val gson: Gson
) : CallAdapter<Type, Call<Result<Type>>> {
    override fun responseType() = type
    override fun adapt(call: Call<Type>): Call<Result<Type>> = ResultCall(call, gson)
}