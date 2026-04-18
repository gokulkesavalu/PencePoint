package co.uk.pencepoint.ui.screens.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.uk.pencepoint.domain.model.Product
import co.uk.pencepoint.domain.repository.BasketRepository
import co.uk.pencepoint.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Product List screen.
 */
@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository,
    basketRepository: BasketRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = combine(
        _uiState,
        basketRepository.getBasketItems()
    ) { state, basketItems ->
        state.copy(basketCount = basketItems.size)
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ProductListUiState()
    )

    init {
        loadProducts()
    }

    internal fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getProducts()
                .onSuccess { products ->
                    _uiState.update { it.copy(products = products, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }
}

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val basketCount: Int = 0
)
