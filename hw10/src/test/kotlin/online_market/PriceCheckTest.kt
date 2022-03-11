package online_market

import online_market.database.Database
import online_market.database.tables.Currencies
import online_market.database.tables.Products
import online_market.database.tables.Users
import online_market.database.tables.toDocument
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * @author Tsutsiev Andrey
 */
internal class PriceCheckTest {
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 1.10,
        "JPY" to 0.0086,
        "GBP" to 1.32
    )

    private lateinit var database: Database

    @BeforeEach
    fun setUp() {
        val currencies = MongoCollectionImpl()
        currencies.insertMany(
            exchangeRates.toList().map{Currencies(it.first, it.second).toDocument()}.toMutableList()
        )
        database = Database(MongoCollectionImpl(), MongoCollectionImpl(), currencies)
    }

    @Test
    fun testConversion() {
        database.run {
            val user1 = Users("Tomas@email.us",    "Tomas",    "USD")
            val user2 = Users("Louis@email.fr",    "Louis",    "EUR")
            val user3 = Users("Soichiro@email.jp", "Soichiro", "JPY")
            val user4 = Users("Louis239@email.uk", "Louis",    "GBP") // same name as user1
            addUsers(mutableListOf(user1, user2, user3, user4))

            val tarelka = Products("Tarelka", "Platinum Tarelka", 29990.0)
            addProduct(tarelka)

            getProductsForUser(user1.email).subscribe {
                assertEquals(exchangeRates[user1.currency]!! * tarelka.price, it.price)
            }
            getProductsForUser(user2.email).subscribe {
                assertEquals(exchangeRates[user2.currency]!! * tarelka.price, it.price)
            }
            getProductsForUser(user3.email).subscribe {
                assertEquals(exchangeRates[user3.currency]!! * tarelka.price, it.price)
            }
        }
    }
}
