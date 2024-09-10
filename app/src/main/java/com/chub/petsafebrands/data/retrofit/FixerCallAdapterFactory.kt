package com.chub.petsafebrands.data.retrofit

import com.google.gson.Gson
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

class FixerCallAdapterFactory @Inject constructor(private val gson: Gson) : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                Result::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    ResultAdapter(resultType, gson)
                }

                else -> null
            }
        }

        else -> null
    }
}