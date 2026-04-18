package co.uk.pencepoint.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.uk.pencepoint.ui.screens.basket.BasketScreen
import co.uk.pencepoint.ui.screens.basket.BasketViewModel
import co.uk.pencepoint.ui.screens.home.HomeScreen
import co.uk.pencepoint.ui.screens.productdetail.ProductDetailScreen
import co.uk.pencepoint.ui.screens.productdetail.ProductDetailsViewModel
import co.uk.pencepoint.ui.screens.productlist.ProductListScreen
import co.uk.pencepoint.ui.screens.productlist.ProductListViewModel

/**
 * Navigation graph of the application.
 * Defines the destinations and the navigation logic between them.
 *
 * @param navController The [NavHostController] used for navigation.
 */
@Composable
fun NavGraph(
    navController: NavHostController
) {
    val actions = PencePointActions(
        onBackClick = { navController.popBackStack() },
        onViewBasketClick = { navController.navigate(Screen.Basket) }
    )
    CompositionLocalProvider(LocalNavActions provides actions) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home
        ) {
            composable<Screen.Home> {
                HomeScreen(
                    onProductListClick = { navController.navigate(Screen.ProductList) },
                    onViewBasketClick = { navController.navigate(Screen.Basket) },
                    onTransactionHistoryClick = { navController.navigate(Screen.TransactionHistory) }
                )
            }
            composable<Screen.ProductList> {
                val viewModel: ProductListViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                ProductListScreen(
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail(productId))
                    },
                    uiState = uiState
                )
            }
            composable<Screen.ProductDetail> { _ ->
                val viewModel: ProductDetailsViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                ProductDetailScreen(
                    uiState = uiState,
                    onAddToCartClick = { product ->
                        viewModel.addToBasket(product, 1)
                    }
                )
            }
            composable<Screen.Basket> {
                val viewModel: BasketViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                BasketScreen(
                    uiState = uiState,
                    updateQuantity = { id, newQuantity ->
                        viewModel.updateQuantity(id, newQuantity)
                    },
                    removeFromBasket = { id ->
                        viewModel.removeFromBasket(id)
                    },
                    onCheckoutClick = { /* Handle checkout */ }
                )
            }
            composable<Screen.TransactionHistory> {
                // Placeholder for History Screen
            }
        }
    }
}
