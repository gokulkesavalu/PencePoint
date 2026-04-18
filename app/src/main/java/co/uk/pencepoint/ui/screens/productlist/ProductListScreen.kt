package co.uk.pencepoint.ui.screens.productlist

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.uk.pencepoint.domain.model.Category
import co.uk.pencepoint.domain.model.Money
import co.uk.pencepoint.domain.model.Product
import co.uk.pencepoint.ui.navigation.LocalNavActions
import co.uk.pencepoint.ui.theme.PencePointTheme
import coil.compose.AsyncImage

/**
 * Entry point for the Product List Screen.
 * Connects the [ProductListViewModel] to the stateless [ProductListContent].
 */
@Composable
fun ProductListScreen(
    onProductClick: (Long) -> Unit,
    uiState: ProductListUiState
) {
    ProductListContent(
        uiState = uiState,
        onProductClick = onProductClick,
    )
}

/**
 * Stateless content for the Product List Screen, ideal for Previews.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListContent(
    uiState: ProductListUiState,
    onProductClick: (Long) -> Unit,
) {
    val actions = LocalNavActions.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = actions.onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = actions.onViewBasketClick) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.products) { product ->
                        ProductItem(
                            product = product,
                            onClick = { onProductClick(product.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual product item in the grid.
 */
@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.price.displayValue,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun ProductListPreview() {
    PencePointTheme {
        ProductListContent(
            uiState = ProductListUiState(
                products = listOf(
                    Product(
                        id = 1,
                        title = "Fjallraven - Backpack",
                        price = Money(10995),
                        description = "Desc",
                        category = Category.OTHER,
                        imageUrl = "",
                        taxRate = 0.0
                    ),
                    Product(
                        id = 2,
                        title = "Mens Casual T-Shirts",
                        price = Money(2230),
                        description = "Desc",
                        category = Category.CLOTHING,
                        imageUrl = "",
                        taxRate = 10.0
                    )
                )
            ),
            onProductClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading - Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Loading - Dark")
@Composable
fun ProductListLoadingPreview() {
    PencePointTheme {
        ProductListContent(
            uiState = ProductListUiState(isLoading = true),
            onProductClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Error - Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Error - Dark")
@Composable
fun ProductListErrorPreview() {
    PencePointTheme {
        ProductListContent(
            uiState = ProductListUiState(error = "No internet connection"),
            onProductClick = {},
        )
    }
}
