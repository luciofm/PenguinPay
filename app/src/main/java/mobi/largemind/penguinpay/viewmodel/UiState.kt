package mobi.largemind.penguinpay.viewmodel

sealed interface UiState {
    object Loading : UiState
    object Error: UiState
    object Loaded : UiState
}