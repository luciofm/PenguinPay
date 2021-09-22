package mobi.largemind.penguinpay.viewmodel

sealed interface SendUiState {
    object None : SendUiState
    object Sending : SendUiState
    data class Error(val fields: List<Field>): SendUiState
    object Sent : SendUiState
}

enum class Field {
    PhoneNumber,
    InvalidPhoneNumber,
    FirstName,
    LastName,
    Amount
}