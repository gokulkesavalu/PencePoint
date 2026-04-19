package co.uk.pencepoint.ui.screens.checkout

/**
 * Sealed class representing one-time events that occur during the checkout process.
 *
 * These events are used to trigger side effects in the UI, such as navigation
 * or showing feedback, in response to check out lifecycle changes.
 */
sealed class CheckoutEvent {

    /**
     * Indicates a successfully completed checkout.
     *
     * @property orderNumber The unique 8-digit order number generated for the transaction.
     */
    data class CheckoutSuccess(val orderNumber: Long) : CheckoutEvent()

}
