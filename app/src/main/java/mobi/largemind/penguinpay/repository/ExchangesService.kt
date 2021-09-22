package mobi.largemind.penguinpay.repository

import mobi.largemind.penguinpay.model.ExchangeRate
import retrofit2.http.GET

interface ExchangesService {
    @GET("/api/latest.json?app_id=4e538adbf91a48a4bf314d18df1262fb")
    suspend fun exchangeRates(): ExchangeRate
}