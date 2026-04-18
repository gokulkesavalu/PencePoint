package co.uk.pencepoint.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.uk.pencepoint.ui.screens.productdetail.ProductDetailScreen
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
    NavHost(
        navController = navController,
        startDestination = Screen.ProductList
    ) {
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
        composable<Screen.ProductDetail> { backStackEntry ->
            val detail: Screen.ProductDetail = backStackEntry.toRoute()
            ProductDetailScreen(
                productId = detail.productId,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}
