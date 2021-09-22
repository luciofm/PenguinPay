package mobi.largemind.penguinpay.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mobi.largemind.penguinpay.model.Country
import mobi.largemind.penguinpay.repository.ExchangeRatessRepository
import java.lang.Exception
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.random.Random

@HiltViewModel
class PenguinPayViewModel @Inject constructor(private val repository: ExchangeRatessRepository)
    : ViewModel() {

    private var rates: Map<String, Float>? = null
    private val currency = MutableLiveData<String>()
    private var currentCountry: Country? = null
    val sendAmount = MutableLiveData<String>()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState>
        get() = _uiState

    private val _sendState = MutableStateFlow<SendUiState>(SendUiState.None)
    val sendState: StateFlow<SendUiState>
        get() = _sendState

    val receiveAmount = Transformations.map(sendAmount) { amount ->
        if (amount.isNullOrEmpty()) {
            return@map "${currency.value}"
        }

        val value = amount.toInt(2)
        val exchange = rates?.get(currency.value) ?: -1f
        if (exchange == -1f) {
            return@map "${currency.value}"
        }
        val converted = (value * exchange).roundToInt()
        Log.d("PenguinPayViewModel", "Converting $value BIN to $converted $currency")
        return@map converted.toUInt().toString(2) + " ${currency.value}"
    }

    val exchangeRate = Transformations.map(currency) { currency ->
        val exchange = rates?.get(currency) ?: -1f
        if (exchange == -1f) {
            return@map ""
        }
        return@map exchange.toUInt().toString(2) + " $currency"
    }

    init {
        loadCurrencies()
    }

    fun loadCurrencies() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                rates = repository.exchangeRates()
                currentCountry?.let {
                    currency.value = it.currency
                }
                _uiState.value = UiState.Loaded
            } catch (ex: Exception) {
                ex.printStackTrace()
                _uiState.value = UiState.Error
            }
        }
    }

    fun setCountry(country: Country) {
        currentCountry = country
        currency.value = country.currency
        sendAmount.value = sendAmount.value
    }

    fun sendMoney(phone: String?, firstName: String?, lastName: String?, amount: String?) {
        requireNotNull(rates) {
            "Exchange rates not loaded"
        }
        requireNotNull(currentCountry) {
            "Country not selected"
        }

        viewModelScope.launch {
            val missingFields = checkFields(phone, firstName, lastName, amount)
            if (missingFields.isNotEmpty()) {
                _sendState.value = SendUiState.Error(missingFields)
                return@launch
            }

            _sendState.value = SendUiState.Sending
            delay(Random.nextLong(500, 900))
            _sendState.value = SendUiState.Sent
        }
    }

    private fun checkFields(
        phone: String?,
        firstName: String?,
        lastName: String?,
        amount: String?
    ): List<Field> {
        val fields = mutableListOf<Field>()
        if (phone.isNullOrEmpty()) {
            fields.add(Field.PhoneNumber)
        } else if (phone.length != currentCountry?.digits ?: 0) {
            fields.add(Field.InvalidPhoneNumber)
        }

        if (firstName.isNullOrEmpty()) fields.add(Field.FirstName)
        if (lastName.isNullOrEmpty()) fields.add(Field.LastName)
        if (amount.isNullOrEmpty()) fields.add(Field.Amount)

        return fields
    }
}