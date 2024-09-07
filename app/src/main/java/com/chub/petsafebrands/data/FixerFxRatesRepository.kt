package com.chub.petsafebrands.data

class FixerFxRatesRepository() : FxRatesRepository {
    override suspend fun getRates(base: String): ExchangeRatesResponse {
        // TODO: Implement real API call
        return ExchangeRatesResponse(
            success = true,
            base = "EUR",
            rates = mapOf(
                "USD" to "1.2",
                "GBP" to "0.8",
                "JPY" to "130.0",
                "AUD" to "1.6",
                "CAD" to "1.5",
                "CHF" to "1.1",
            ))
    }
}