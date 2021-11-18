package database.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.atsutsiev.sd.refactoring.database.models.Products
import ru.atsutsiev.sd.refactoring.database.models.custom_fields.ProductDataRecord
import java.sql.SQLException

/**
 * @author atsutsuiev
 */
class ProductsTest {
    companion object {
        private var productsList = listOf(
            ProductDataRecord("vilka", 2),
            ProductDataRecord("lozhka", 1),
            ProductDataRecord("tarelka", 15),
            ProductDataRecord("itilnaya_tarelka", 239),
            ProductDataRecord("deshmanskaya_miska", 10),
        )

        private var productsModel = Products().apply { create() }

    }

    @Before
    fun fillModel() { productsModel.apply { clean(); productsList.forEach(this::insert) } }

    @After
    @Throws(Exception::class)
    fun cleanUp() { productsModel.clean() }

    @Test
    @Throws(SQLException::class)
    fun testGetAll() {
        assertThat(productsModel.all()).containsExactlyInAnyOrder(
            productsList[0],
            productsList[1],
            productsList[2],
            productsList[3],
            productsList[4],
        )
    }

    @Test
    @Throws(SQLException::class)
    fun testMax() {
        productsModel.max().apply {
            assertThat(isPresent)
            assertThat(get()).isEqualTo(ProductDataRecord("itilnaya_tarelka", 239))
        }
    }

    @Test
    @Throws(SQLException::class)
    fun testMin() {
        productsModel.min().apply {
            assertThat(isPresent)
            assertThat(get()).isEqualTo(ProductDataRecord("lozhka", 1))
        }
    }
    @Test
    @Throws(SQLException::class)
    fun testCount() {
        assertThat(productsModel.count()).isEqualTo(5)
    }

    @Test
    @Throws(SQLException::class)
    fun testSum() {
        assertThat(productsModel.sum()).isEqualTo(267)
    }
}
