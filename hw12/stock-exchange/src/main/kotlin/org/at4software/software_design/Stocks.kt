package org.at4software.software_design

import org.junit.Assert.assertTrue

/**
 * @author Tsutsiev Andrey
 */
class Stocks {
    public class Stock(val id: Int, val name: String?, val price: Double, val amount: Int)

    private val stocks: MutableList<Stock?> = ArrayList()

    fun getStock(companyId: Int): Stock? = if (companyId < stocks.size) stocks[companyId] else null

    fun addCompanyStocks(name: String): Int {
        stocks.add(Stock(stocks.size, name, .0, 0))
        return stocks.size
    }

    fun addStocks(companyId: Int, amount: Int): Boolean {
        if (companyId >= stocks.size || amount <= 0) return false
        val stock = stocks[companyId]!!
        stocks[companyId] = Stock(stock.id, stock.name, stock.price, stock.amount + amount)
        return true
    }

    fun changeStockPrice(companyId: Int, delta: Double): Boolean {
        if (companyId >= stocks.size) return false
        val stock = stocks[companyId]!!
        stocks[companyId] = Stock(stock.id, stock.name, stock.price + delta, stock.amount)
        return true
    }

    fun buyStock(companyId: Int, amount: Int): Boolean {
        if (companyId >= stocks.size || amount <= 0) return false
        val stock = stocks[companyId]!!
        assertTrue(stock.amount - amount >= 0)
        stocks[companyId] = Stock(stock.id, stock.name, stock.price, stock.amount + amount)
        return true
    }

    fun sellStock(companyId: Int, amount: Int): Double {
        if (companyId >= stocks.size || amount <= 0) return .0
        val stock = stocks[companyId]!!
        stocks[companyId] = Stock(stock.id, stock.name, stock.price, stock.amount + amount)
        return stock.price * amount
    }
}
