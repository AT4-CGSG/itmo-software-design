package ru.atsutsiev.sd.refactoring.database.models

import ru.atsutsiev.sd.refactoring.database.models.custom_fields.ProductDataRecord
import java.sql.*
import java.util.*
import java.util.stream.Collectors.joining

/**
 * @author atsutsiev
 */
class Products(private val databaseURL: String) {
    @get:Throws(SQLException::class)
    val connection: Connection
        get() = DriverManager.getConnection(databaseURL)

    @Throws(IllegalArgumentException::class, SQLException::class)
    fun queryAsHTML(queryCommand: String?): String {
        return when (queryCommand) {
            "all"   -> toHTML(all())
            "sum"   -> toHTML("Summary price: ", sum())
            "count" -> toHTML("Number of products: ", count())
            "max" -> {
                val header = "ProductDataRecord with max price: "
                max().map { p: ProductDataRecord -> toHTML(header, p) }.orElseGet { toHTML(header) }
            }
            "min" -> {
                val header = "ProductDataRecord with min price: "
                min().map { p: ProductDataRecord -> toHTML(header, p) }.orElseGet { toHTML(header) }
            }
            else -> "Products table: available queries: $AVAILABLE_QUERIES"
        }
    }

    @Throws(SQLException::class)
    fun insert(product: ProductDataRecord): Int =
        statement().executeUpdate("INSERT INTO $TABLE_NAME (NAME, PRICE) VALUES (\"${product.name}\",${product.price})")

    @Throws(SQLException::class)
    fun create(): Int = statement().executeUpdate(
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME" +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " NAME           TEXT    NOT NULL, " +
            " PRICE          INT     NOT NULL)"
    )

    @Throws(SQLException::class)
    fun clean(): Int = statement().executeUpdate("DELETE FROM $TABLE_NAME")

    /* Queries */
    
    @Throws(SQLException::class)
    fun all(): List<ProductDataRecord> =
        parseQueryOutput(statement().executeQuery("SELECT * FROM $TABLE_NAME"))

    @Throws(SQLException::class)
    fun max(): Optional<ProductDataRecord> =
        parseQueryOutput(statement().executeQuery("SELECT * FROM $TABLE_NAME ORDER BY PRICE DESC LIMIT 1"))
            .stream()
            .findFirst()

    @Throws(SQLException::class)
    fun min(): Optional<ProductDataRecord> =
        parseQueryOutput(statement().executeQuery("SELECT * FROM $TABLE_NAME ORDER BY PRICE LIMIT 1"))
            .stream()
            .findFirst()

    @Throws(SQLException::class)
    fun sum(): Long =
        statement().executeQuery("SELECT SUM(price) as sum FROM $TABLE_NAME").getLong("sum")

    @Throws(SQLException::class)
    fun count(): Int =
        statement().executeQuery("SELECT COUNT(*) as cnt FROM $TABLE_NAME").getInt("cnt")

    /* Internals */

    @Throws(SQLException::class)
    private fun statement(): Statement = connection.createStatement()

    @Throws(SQLException::class)
    private fun parseQueryOutput(rs: ResultSet): List<ProductDataRecord> = ArrayList<ProductDataRecord>().apply {
        rs.run { while (next()) add(ProductDataRecord(getString("name"), getInt("price").toLong())) }
    }

    companion object {
        private const val TABLE_NAME = "PRODUCT"
        private const val AVAILABLE_QUERIES = "max, min, sum, count, all"

        private fun toHTML(header: String): String = "<html><body>\n<h1>$header</h1>\b</body></html>"

        private fun toHTML(header: String, product: ProductDataRecord): String =
            "<html><body>\n<h1>$header</h1>\n${product.toHTML()}\n</body></html>"

        private fun toHTML(header: String, info: Any): String = "<html><body>\n$header\n$info\n</body></html>"

        private fun toHTML(products: List<ProductDataRecord>): String = """
            <html><body>
            ${products.stream().map(ProductDataRecord::toHTML).collect(joining("\n"))}
            </body></html>
            """.trimIndent()
    }
}