package org.at4software.software_design

import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Tsutsiev Andrey
 */
@RestController
class StocksController {
    private val stocks = Stocks()

    @RequestMapping("/stock_price")
    fun getStockPrice(id: Int): ResponseEntity<Double> =
        stocks.getStock(id)?.let { ok(it.price) } ?: status(BAD_REQUEST).body(null)

    @RequestMapping("/stocks_amount")
    fun getStocksAmount(id: Int): ResponseEntity<Int> =
        stocks.getStock(id)?.let { ok(it.amount) } ?: status(BAD_REQUEST).body(null)

    @RequestMapping("/add_company")
    fun addCompany(name: String): Int = stocks.addCompanyStocks(name)

    @RequestMapping("/add_stocks")
    fun addStocks(id: Int, amount: Int): ResponseEntity<Int> =
        if (stocks.addStocks(id, amount)) ok(amount) else status(BAD_REQUEST).body(null)

    @RequestMapping("/change_stock_cost")
    fun changeStock(id: Int, delta: Double): ResponseEntity<Double> =
        if (stocks.changeStockPrice(id, delta)) ok(delta) else status(BAD_REQUEST).body(null)

    @RequestMapping("/buy_stock")
    fun buyStock(id: Int, amount: Int): ResponseEntity<Int?>? =
        if (stocks.buyStock(id, amount)) ok(amount) else status(BAD_REQUEST).body(null)

    @RequestMapping("/sell_stock")
    fun sellStock(id: Int, amount: Int): HttpEntity<Double> =
        stocks.sellStock(id, amount).let { return@sellStock if (it != 0.0) ok(it) else status(BAD_REQUEST).body(null) }
}
