package online_market.database.tables

import com.google.gson.annotations.SerializedName
import org.bson.Document

/**
 * @author Tsutsiev Andrey
 */
data class Products(
    @SerializedName("title")       val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price")       val price: Double
)
fun Products.toDocument() = Document(mapOf(
    "title"       to title,
    "description" to description,
    "price"       to price
))
fun Document.toProducts() = Products(getString("title"), getString("description"), getDouble("price"))
fun Products.toProductsWithScaledPrice(multiplier: Double) = Products(title, description, price * multiplier)
