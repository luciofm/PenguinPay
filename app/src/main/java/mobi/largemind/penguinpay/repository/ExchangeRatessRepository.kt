package mobi.largemind.penguinpay.repository

import mobi.largemind.penguinpay.model.toMap

class ExchangeRatessRepository(private val service: ExchangeRateService) {
    suspend fun exchangeRates(): Map<String, Float> {
        return service.exchangeRates().toMap()
    }
}