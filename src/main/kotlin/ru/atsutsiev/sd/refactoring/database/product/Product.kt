package ru.atsutsiev.sd.refactoring.database.product

import java.util.*

/**
 * @author atsutsiev
 */
class Product(val name: String, val price: Long) {
    fun toHTML(): String = "$name\t$price</br>"

    override fun toString(): String = "Product {  name: $name price: $price}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val product = other as Product
        return price == product.price && name == product.name
    }

    override fun hashCode(): Int = Objects.hash(name, price)
}