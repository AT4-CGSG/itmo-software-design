package online_market.database.tables

import com.google.gson.annotations.SerializedName
import org.bson.Document

/**
 * @author Tsutsiev Andrey
 */
data class Currencies(
    @SerializedName("code")       val code: String,
    @SerializedName("multiplier") val multiplier: Double
)
fun Currencies.toDocument() = Document(mapOf(
    "code"       to code,
    "multiplier" to multiplier
))
fun Document.toCurrencies() = Currencies(getString("code"), getDouble("multiplier"))
