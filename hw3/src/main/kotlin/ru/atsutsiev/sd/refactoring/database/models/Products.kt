package ru.atsutsiev.sd.refactoring.database.models

import ru.atsutsiev.sd.refactoring.database.models.custom_fields.ProductDataRecord
import java.sql.*
import java.util.*
import java.util.stream.Collectors.joining

/**
 * @author atsutsiev
 */
open class Products {
    @Throws(IllegalArgumentException::class, SQLException::class)
    fun queryAsHTML(queryCommand: String?): String {
        return when (queryCommand) {
            "all"   -> toHTML(all())
            "sum"   -> toHTML("Summary price:", sum())
            "count" -> toHTML("Amount of products:", count())
            "max" -> {
                max().map { p: ProductDataRecord -> toHTML("The most expensive product:", p) }
                    .orElseGet { toHTML("No products") }
            }
            "min" -> {
                min().map { p: ProductDataRecord -> toHTML("The least expensive product:", p) }
                    .orElseGet { toHTML("No products") }
            }
            else -> "Products table: available queries: $AVAILABLE_QUERIES"
        }
    }

    @Throws(SQLException::class)
    fun insert(product: ProductDataRecord): Int {
        statement().use {
            return@insert it.executeUpdate("INSERT INTO $TABLE_NAME (NAME, PRICE) VALUES (\"${product.name}\",${product.price})")
        }
    }

    @Throws(SQLException::class)
    fun create(): Int {
        statement().use {
            return@create it.executeUpdate("CREATE TABLE IF NOT EXISTS $TABLE_NAME" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)")
        }
    }

    @Throws(SQLException::class)
    fun clean(): Int { statement().use { return@clean it.executeUpdate("DELETE FROM $TABLE_NAME") } }

    /* Queries */
    
    @Throws(SQLException::class)
    open fun all(): List<ProductDataRecord> {
        statement().use { return@all parseQueryOutput(it.executeQuery("SELECT * FROM $TABLE_NAME")) }
    }

    @Throws(SQLException::class)
    open fun max(): Optional<ProductDataRecord> {
        statement().use {
            return@max parseQueryOutput(it.executeQuery("SELECT * FROM $TABLE_NAME ORDER BY PRICE DESC LIMIT 1")).stream().findFirst()
        }
    }

    @Throws(SQLException::class)
    open fun min(): Optional<ProductDataRecord> {
        statement().use {
            return@min parseQueryOutput(it.executeQuery("SELECT * FROM $TABLE_NAME ORDER BY PRICE LIMIT 1")).stream().findFirst()
        }
    }

    @Throws(SQLException::class)
    open fun sum(): Long {
        statement().use {
            return@sum it.executeQuery("SELECT SUM(PRICE) as sum FROM $TABLE_NAME").getLong("sum")
        }
    }

    @Throws(SQLException::class)
    open fun count(): Int {
        statement().use {
            return@count it.executeQuery("SELECT COUNT(*) as cnt FROM $TABLE_NAME").getInt("cnt")
        }
    }

    companion object {
        private const val TABLE_NAME = "PRODUCT"
        private const val AVAILABLE_QUERIES = "max, min, sum, count, all"

        private const val databaseURL = "jdbc:sqlite:test.db"

        @Throws(SQLException::class)
        private fun statement(): Statement = connection.createStatement()

        @get:Throws(SQLException::class)
        val connection: Connection
            get() = DriverManager.getConnection(databaseURL)

        @Throws(SQLException::class)
        private fun parseQueryOutput(rs: ResultSet): List<ProductDataRecord> = ArrayList<ProductDataRecord>().apply {
            rs.run { while (next()) add(ProductDataRecord(getString("name"), getInt("price").toLong())) }
        }

        @Suppress("SameParameterValue")
        private fun toHTML(header: String): String =
            "<html><body>\n<h1>$header</h1>\b</body></html>"

        private fun toHTML(header: String, product: ProductDataRecord): String =
            "<html><body>\n<h1>$header</h1>\n${product.toHTML()}\n</body></html>"

        private fun toHTML(header: String, info: Any): String =
            "<html><body>\n<h1>$header\n</h1><br>$info\n</br></body></html>"

        private fun toHTML(products: List<ProductDataRecord>): String =
            "<html><body>" +
            products.stream().map(ProductDataRecord::toHTML).collect(joining("\n")) +
            "</body></html>"
    }
}
