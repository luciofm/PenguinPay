package mobi.largemind.penguinpay.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExchangeRate(
    val rates: Rates
)

data class Rates(
    @Json(name = "KES")
    val kes: Float,
    @Json(name = "NGN")
    val ngn: Float,
    @Json(name = "TZS")
    val tzs: Float,
    @Json(name = "UGX")
    val ugx: Float
)

fun ExchangeRate.toMap(): Map<String, Float> = mapOf(
    "KES" to rates.kes,
    "NGN" to rates.ngn,
    "TZS" to rates.tzs,
    "UGX" to rates.ugx
)