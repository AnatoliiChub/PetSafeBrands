package com.chub.petsafebrands.config

/**
 * It's not extracted to separate flavor because it's not a real project and I don't want to overcomplicate it.
 */
object DebugConfig {
    //just to avoid additional connection to fixer api since amount of available requests is limited
    //I used google.com as a host for mock api to avoid Bad gateway error (502).
    const val BASE_URL = "https://google.com/"

    const val MOCK_RESPONSE_HEADER: String = "X-Mock-Response-File"
    const val SUCCESS_CODE = 200
    const val ERROR_CODE = 400
    const val IS_ERROR_RESPONSE = false
}