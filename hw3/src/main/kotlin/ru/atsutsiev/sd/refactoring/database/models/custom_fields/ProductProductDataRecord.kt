package ru.atsutsiev.sd.refactoring.database.models.custom_fields

import java.util.*

/**
 * @author atsutsiev
 */
class ProductDataRecord(val name: String, val price: Long) {
    fun toHTML(): String = "$name\t$price</br>"

    override fun toString(): String = "Product {  name: $name price: $price}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val product = other as ProductDataRecord
        return price == product.price && name == product.name
    }

    override fun hashCode(): Int = Objects.hash(name, price)
}