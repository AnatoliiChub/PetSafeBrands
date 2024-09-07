package com.chub.petsafebrands.data.debug

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
        val currency = headers[MOCK_RESPONSE_HEADER]
        val file = "$api/${currency}_response.json"
        val responseString = jsonReader.getJsonAsString(file)

        return chain.proceed(chain.request())
            .newBuilder()
            .code(SUCCESS_CODE)
            .message(responseString)
            .body(responseString.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json").build()
    }
}