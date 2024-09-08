package com.chub.petsafebrands.data.debug

import com.chub.petsafebrands.config.DebugConfig.ERROR_CODE
import com.chub.petsafebrands.config.DebugConfig.IS_ERROR_RESPONSE
import com.chub.petsafebrands.config.DebugConfig.MOCK_RESPONSE_HEADER
import com.chub.petsafebrands.config.DebugConfig.SUCCESS_CODE
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class FakeResponseInterceptor @Inject constructor(private val jsonReader: JsonReader) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        Thread.sleep(1500)

        val headers = chain.request().headers
        val api = chain.request().url.toString().substringAfterLast("/")
        val header = headers[MOCK_RESPONSE_HEADER]
        val file = if (IS_ERROR_RESPONSE) "error.json" else {
            when (api) {
                "latest" -> "$api/${header}_response.json"
                "historical" -> "$api/response.json"
                else -> "error.json"
            }
        }
        val responseString = jsonReader.getJsonAsString(file)

        return chain.proceed(chain.request()).use {
            it.newBuilder()
                .code(if (IS_ERROR_RESPONSE) ERROR_CODE else SUCCESS_CODE)
                .message(responseString)
                .body(responseString.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
                .addHeader("content-type", "application/json").build()
        }
    }
}