package org.at4software.software_design

/**
 * @author Tsutsiev Andrey
 */
class User(val id: Int, val name: String, var money: Double, val stocks: MutableMap<Int, Int>) {
    fun addMoney(delta: Double) { money += delta }
}
