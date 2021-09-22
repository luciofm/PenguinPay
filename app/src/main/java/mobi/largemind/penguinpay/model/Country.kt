package mobi.largemind.penguinpay.model

data class Country(
    val country: String,
    val flag: String,
    val currency: String,
    val phonePrefix: String,
    val digits: Int
)

val countries = listOf(
    Country("Kenya", "\uD83C\uDDF0\uD83C\uDDEA", "KES", "+254", 9),
    Country("Nigeria", "\uD83C\uDDF3\uD83C\uDDEC", "NGN", "+234", 7),
    Country("Tanzania", "\uD83C\uDDF9\uD83C\uDDFF", "TZS", "+255", 9),
    Country("Uganda", "\uD83C\uDDFA\uD83C\uDDEC", "UGX", "+256", 7)
)
