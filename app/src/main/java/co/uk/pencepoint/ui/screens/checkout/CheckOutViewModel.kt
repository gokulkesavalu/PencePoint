package co.uk.pencepoint.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.uk.pencepoint.domain.usecases.ProcessCheckoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckOutViewModel @Inject constructor(
    private val processCheckoutUseCase: ProcessCheckoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckOutUiState())
    val uiState: StateFlow<CheckOutUiState> = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<CheckoutEvent>()
    val event: SharedFlow<CheckoutEvent> = _event.asSharedFlow()

    fun checkout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            // Simulate network delay
            delay(3000)
            processCheckoutUseCase().onSuccess { transaction ->
                _uiState.update { it.copy(isLoading = false) }
                _event.emit(CheckoutEvent.CheckoutSuccess(transaction.orderNumber))
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "An error occurred while processing."
                    )
                }
            }
        }
    }
}

data class CheckOutUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)