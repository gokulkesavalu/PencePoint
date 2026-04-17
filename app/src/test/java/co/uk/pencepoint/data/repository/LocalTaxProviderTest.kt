package co.uk.pencepoint.data.repository

import co.uk.pencepoint.domain.model.Category
import co.uk.pencepoint.domain.model.Money
import co.uk.pencepoint.domain.repository.TaxProvider
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [LocalTaxProvider].
 */
class LocalTaxProviderTest {

    private val taxProvider: TaxProvider = LocalTaxProvider()

    private val dummyPrice = Money(1000) // £10.00

    @Test
    fun `getTaxRate for ELECTRONICS returns 15 percent`() {
        val rate = taxProvider.getTaxRate(Category.ELECTRONICS, dummyPrice)
        assertEquals(15.0, rate, 0.0)
    }

    @Test
    fun `getTaxRate for CLOTHING returns 10 percent`() {
        val rate = taxProvider.getTaxRate(Category.CLOTHING, dummyPrice)
        assertEquals(10.0, rate, 0.0)
    }

    @Test
    fun `getTaxRate for FOOD returns 5 percent`() {
        val rate = taxProvider.getTaxRate(Category.FOOD, dummyPrice)
        assertEquals(5.0, rate, 0.0)
    }

    @Test
    fun `getTaxRate for BOOKS returns 3 percent`() {
        val rate = taxProvider.getTaxRate(Category.BOOKS, dummyPrice)
        assertEquals(3.0, rate, 0.0)
    }

    @Test
    fun `getTaxRate for JEWELLERY returns 20 percent`() {
        val rate = taxProvider.getTaxRate(Category.JEWELLERY, dummyPrice)
        assertEquals(20.0, rate, 0.0)
    }

    @Test
    fun `getTaxRate for OTHER returns 0 percent`() {
        val rate = taxProvider.getTaxRate(Category.OTHER, dummyPrice)
        assertEquals(0.0, rate, 0.0)
    }
}
