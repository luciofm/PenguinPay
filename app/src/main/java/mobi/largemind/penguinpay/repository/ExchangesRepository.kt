package mobi.largemind.penguinpay.repository

import mobi.largemind.penguinpay.model.toMap

class ExchangesRepository(private val service: ExchangesService) {
    suspend fun exchanges(): Map<String, Float> {
        return service.exchangeRates().toMap()
    }
}