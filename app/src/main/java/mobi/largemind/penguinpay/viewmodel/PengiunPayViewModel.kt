package mobi.largemind.penguinpay.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mobi.largemind.penguinpay.repository.ExchangesRepository
import javax.inject.Inject

@HiltViewModel
class PengiunPayViewModel @Inject constructor(private val respository: ExchangesRepository) : ViewModel() {
}