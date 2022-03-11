package online_market.database.tables

import com.google.gson.annotations.SerializedName
import org.bson.Document

/**
 * @author Tsutsiev Andrey
 */
data class Users(
    @SerializedName("email")    val email:    String,
    @SerializedName("name")     val name:     String,
    @SerializedName("currency") val currency: String
)
fun Users.toDocument() = Document(mapOf(
    "email"    to email,
    "name"     to name,
    "currency" to currency
))
fun Document.toUsers() = Users(getString("email"), getString("name"), getString("currency"))
