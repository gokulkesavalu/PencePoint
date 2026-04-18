package co.uk.pencepoint.ui.screens.basket

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.uk.pencepoint.domain.model.BasketItem
import co.uk.pencepoint.domain.model.Category
import co.uk.pencepoint.domain.model.Money
import co.uk.pencepoint.domain.model.Product
import co.uk.pencepoint.ui.navigation.LocalNavActions
import co.uk.pencepoint.ui.navigation.PencePointActions
import co.uk.pencepoint.ui.theme.PencePointTheme
import coil.compose.AsyncImage

/**
 * Screen displaying the items in the user's shopping basket.
 *
 * @param updateQuantity Callback for updating an item's quantity.
 * @param removeFromBasket Callback for removing an item from the basket.
 * @param uiState Current state of the basket UI.
 * @param onCheckoutClick Callback for the checkout action.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasketScreen(
    updateQuantity: (Long, Int) -> Unit,
    removeFromBasket: (Long) -> Unit,
    uiState: BasketUiState,
    onCheckoutClick: () -> Unit
) {
    val actions = LocalNavActions.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Basket") },
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
                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(text = uiState.basketItems.size.toString())
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "View Basket"
                            )
                        }
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
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                else -> {
                    if (uiState.basketItems.isEmpty()) {
                        Text(
                            text = "Your basket is empty",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        BasketItemList(
                            items = uiState.basketItems,
                            updateQuantity = updateQuantity,
                            removeFromBasket = removeFromBasket,
                            onCheckoutClick = onCheckoutClick
                        )
                    }
                }
            }
        }
    }
}

/**
 * List of basket items with a summary section.
 */
@Composable
fun BasketItemList(
    items: List<BasketItem>,
    updateQuantity: (Long, Int) -> Unit,
    removeFromBasket: (Long) -> Unit,
    onCheckoutClick: () -> Unit
) {
    val totalSubtotal = items.fold(Money(0)) { acc, item -> acc + item.subtotal }
    val totalTax = items.fold(Money(0)) { acc, item -> acc + item.taxAmount }
    val totalPrice = totalSubtotal + totalTax

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            BasketItemRow(
                item = item,
                updateQuantity = { newQuantity ->
                    updateQuantity(item.id, newQuantity)
                },
                removeFromBasket = {
                    removeFromBasket(item.id)
                }
            )
        }

        item {
            BasketSummary(
                subtotal = totalSubtotal,
                tax = totalTax,
                total = totalPrice,
                onCheckoutClick = onCheckoutClick
            )
        }
    }
}

/**
 * Summary section displaying totals and checkout button.
 */
@Composable
fun BasketSummary(
    subtotal: Money,
    tax: Money,
    total: Money,
    onCheckoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SummaryRow(label = "Subtotal", value = subtotal.displayValue)
        SummaryRow(label = "Estimated Tax", value = tax.displayValue)

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        SummaryRow(
            label = "Total",
            value = total.displayValue,
            isTotal = true
        )

        Button(
            onClick = onCheckoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Checkout")
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (isTotal) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}

/**
 * Individual row displaying product information in the basket.
 */
@Composable
fun BasketItemRow(
    item: BasketItem,
    updateQuantity: (Int) -> Unit,
    removeFromBasket: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Small Product Image
        AsyncImage(
            model = item.product.imageUrl,
            contentDescription = item.product.title,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(0.5.dp, Color.LightGray, RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Fit
        )

        // Product Details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.product.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { if (item.quantity > 1) updateQuantity(item.quantity - 1) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = item.quantity.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(
                    onClick = { updateQuantity(item.quantity + 1) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "x ${item.product.price.displayValue}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        // Subtotal Price
        Text(
            text = item.subtotal.displayValue,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )

        // Delete Button
        IconButton(onClick = removeFromBasket) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun BasketItemRowPreview() {
    PencePointTheme {
        BasketItemRow(
            item = BasketItem(
                id = 1,
                product = Product(
                    id = 1,
                    title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                    price = Money(10995),
                    description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
                    category = Category.OTHER,
                    imageUrl = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
                    taxRate = 20.0
                ),
                quantity = 2
            ),
            updateQuantity = {},
            removeFromBasket = {}
        )
    }
}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun BasketScreenPreview() {
    PencePointTheme {
        CompositionLocalProvider(
            LocalNavActions provides PencePointActions(
                onBackClick = {},
                onViewBasketClick = {}
            )
        ) {
            BasketScreen(
                updateQuantity = { _, _ -> },
                removeFromBasket = { _ -> },
                uiState = BasketUiState(
                    basketItems = listOf(
                        BasketItem(
                            id = 1,
                            product = Product(
                                id = 1,
                                title = "Backpack",
                                price = Money(10995),
                                description = "Desc",
                                category = Category.OTHER,
                                imageUrl = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
                                taxRate = 20.0
                            ),
                            quantity = 1
                        ),
                        BasketItem(
                            id = 2,
                            product = Product(
                                id = 2,
                                title = "T-Shirt",
                                price = Money(2230),
                                description = "Desc",
                                category = Category.OTHER,
                                imageUrl = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
                                taxRate = 20.0
                            ),
                            quantity = 3
                        )
                    )
                ),
                onCheckoutClick = {}
            )
        }
    }
}
