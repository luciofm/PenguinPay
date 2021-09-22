package mobi.largemind.penguinpay.repository

import mobi.largemind.penguinpay.model.ExchangeRateResponse
import retrofit2.http.GET

interface ExchangeRateService {
    @GET("/api/latest.json?app_id=4e538adbf91a48a4bf314d18df1262fb")
    suspend fun exchangeRates(): ExchangeRateResponse
}