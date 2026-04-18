package co.uk.pencepoint.ui.navigation

import androidx.compose.runtime.staticCompositionLocalOf

data class PencePointActions(
    val onBackClick: () -> Unit,
    val onViewBasketClick: () -> Unit,
)

val LocalNavActions = staticCompositionLocalOf<PencePointActions> {
    error("No PencePointActions provided")
}