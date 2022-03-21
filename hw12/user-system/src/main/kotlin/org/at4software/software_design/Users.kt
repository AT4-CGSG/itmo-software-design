package org.at4software.software_design

/**
 * @author Tsutsiev Andrey
 */
class Users(private val client: StocksClient) {
    private val users: MutableList<User> = ArrayList()

    fun addUser(name: String): Int {
        users.add(User(users.size, name, .0, HashMap()))
        return users.size
    }

    fun addMoney(userId: Int, money: Double): Boolean {
        if (userId >= users.size) return false
        val user = users[userId]
        users[userId] = User(user.id, user.name, user.money + money, user.stocks)
        return true
    }

    fun getUserStocks(userId: Int): MutableList<Stock> {
        if (userId >= users.size) return ArrayList()
        return emptyList<Stock>().toMutableList().also {
            users[userId].stocks.forEach { (id, amount) ->
                client.getStockPrice(id).let { price -> if (price != null) it += Stock(id, price, amount) }
            }
        }
    }

    fun getUserMoney(userId: Int): Double? =
        if (userId < users.size) users[userId].money + getUserStocks(userId).sumOf { it.price * it.amount }
        else null

    fun buyStock(userId: Int, companyId: Int, amount: Int): Boolean {
        if (userId >= users.size) return false
        users[userId].apply {
            val cost = client.getStockPrice(companyId) ?: .0
            return@buyStock if (cost * amount < money && client.buyStocks(companyId, amount)) {
                stocks[companyId] = stocks.getOrDefault(companyId, 0) + amount
                addMoney(-cost * amount)
                true
            } else false
        }
    }

    fun isStockSold(userId: Int, companyId: Int, amount: Int): Boolean {
        if (userId >= users.size) return false
        users[userId].apply {
            return@isStockSold if (stocks.getOrDefault(companyId, 0) >= amount && client.sellStocks(companyId, amount)) {
                stocks[companyId] = stocks.getOrDefault(companyId, 0) - amount
                addMoney((client.getStockPrice(companyId) ?: .0) * amount)
                true
            } else false
        }
    }
}