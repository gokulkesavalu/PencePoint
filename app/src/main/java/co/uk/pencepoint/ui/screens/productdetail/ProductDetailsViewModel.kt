package co.uk.pencepoint.ui.screens.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.uk.pencepoint.domain.model.Product
import co.uk.pencepoint.domain.repository.BasketRepository
import co.uk.pencepoint.domain.repository.ProductRepository
import co.uk.pencepoint.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val basketRepository: BasketRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = combine(
        _uiState,
        basketRepository.getBasketItems()
    ) { state, basketItems ->
        state.copy(basketCount = basketItems.size)
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ProductDetailsUiState()
    )

    init {
        val detailRoute = savedStateHandle.toRoute<Screen.ProductDetail>()
        loadProductDetails(detailRoute.productId)
    }

    internal fun loadProductDetails(productId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            productRepository.getProductDetails(productId)
                .onSuccess { product ->
                    _uiState.value = _uiState.value.copy(product = product, isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message, isLoading = false)
                }
        }
    }

    internal fun addToBasket(product: Product, quantity: Int) {
        viewModelScope.launch {
            basketRepository.addToBasket(product, quantity).onFailure {
                _uiState.value = _uiState.value.copy(error = it.message)
            }
        }
    }
}

data class ProductDetailsUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val basketCount: Int = 0
)