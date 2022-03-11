package online_market.database

import com.mongodb.rx.client.MongoCollection
import com.mongodb.rx.client.Success
import online_market.database.tables.*
import org.bson.Document
import rx.Observable

/**
 * @author Tsutsiev Andrey
 */
open class Database(
        private var usersTable: MongoCollection<Document>,
        private var productsTable: MongoCollection<Document>,
        private var currenciesTable: MongoCollection<Document>
) {
    fun addUser(user: Users): Observable<Success> = usersTable.insertOne(user.toDocument())

    fun addUsers(users: MutableList<out Users>?): Observable<Success> = usersTable.insertMany(users!!.map { it.toDocument() })

    fun addProduct(product: Products): Observable<Success> = productsTable.insertOne(product.toDocument())

    fun addProducts(products: MutableList<out Products>?): Observable<Success> = productsTable.insertMany(products!!.map { it.toDocument() })

    fun getProductsForUser(email: String): Observable<Products> =
        usersTable.find(Document("email", email))
                .toObservable()
                .singleOrDefault(null)
                .filter { d -> d != null }
                .map { it.toUsers().currency }
                .flatMap { currency ->
                    currenciesTable
                            .find(Document("currency", currency))
                            .toObservable()
                            .map { it.toCurrencies().multiplier }
                }
                .flatMap { multiplier ->
                    productsTable.find()
                            .toObservable()
                            .map { it.toProducts().toProductsWithScaledPrice(multiplier) }
                }
}