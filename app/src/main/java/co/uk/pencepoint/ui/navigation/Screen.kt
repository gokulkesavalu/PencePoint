package co.uk.pencepoint.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Sealed class representing the different screens in the application.
 * Uses type-safe navigation with Kotlin Serialization.
 */
sealed interface Screen {
    
    @Serializable
    data object ProductList : Screen

    @Serializable
    data class ProductDetail(val productId: Long) : Screen
}
