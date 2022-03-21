package org.at4software.software_design

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
class UserController {
    private val client: StocksClient = StocksClient("http://127.0.0.1", 8080)
    private val database: Users = Users(client)

    @RequestMapping("/get_user_stocks")
    fun getUserStocks(id: Int): MutableList<Stock> = database.getUserStocks(id)

    @RequestMapping("/get_user_money")
    fun getUserMoney(id: Int): ResponseEntity<Double> =
        database.getUserMoney(id)?.let { ok(it) } ?: status(BAD_REQUEST).body(null)

    @RequestMapping("/add_user")
    fun addUser(name: String): Int = database.addUser(name)

    @RequestMapping("/add_money")
    fun addMoney(id: Int, money: Double): ResponseEntity<String> =
        if (database.addMoney(id, money)) ok("") else status(BAD_REQUEST).body(null)

    @RequestMapping("/buy_stock")
    fun buyStock(userId: Int, companyId: Int, amount: Int): ResponseEntity<String?>? =
        if (database.buyStock(userId, companyId, amount)) ok("") else status( BAD_REQUEST).body(null)

    @RequestMapping("/sold_stock")
    fun soldStock(userId: Int, companyId: Int, amount: Int): ResponseEntity<String?>? =
        if (database.isStockSold(userId, companyId, amount)) ok("") else status(BAD_REQUEST).body(null)
}