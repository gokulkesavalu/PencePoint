package co.uk.pencepoint.ui.screens.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.uk.pencepoint.domain.model.BasketItem
import co.uk.pencepoint.domain.repository.BasketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val repository: BasketRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BasketUiState())
    val uiState: StateFlow<BasketUiState> = _uiState.asStateFlow()

    init {
        loadBasketItems()
    }

    internal fun loadBasketItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getBasketItems()
                .collect { items ->
                    _uiState.update {
                        it.copy(
                            basketItems = items,
                            isLoading = false
                        )
                    }
                }
        }
    }

    internal fun updateQuantity(id: Long, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateQuantity(id, newQuantity).onSuccess {
                loadBasketItems()
            }.onFailure {
                _uiState.update { it.copy(error = it.error) }
            }
        }
    }

    internal fun removeFromBasket(id: Long) {
        viewModelScope.launch {
            repository.removeFromBasket(id).onSuccess {
                loadBasketItems()
            }.onFailure {
                _uiState.update { it.copy(error = it.error) }
            }
        }
    }
}

data class BasketUiState(
    val basketItems: List<BasketItem> = emptyList(),
    val totalPrice: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)