package org.at4software.software_design

import org.junit.Assert.*
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.GenericContainer

/**
 * @author Tsutsiev Andrey
 */
open class UserSystemTest {
    companion object {
        @ClassRule @JvmStatic
        var server: GenericContainer<*> = FixedHostPortGenericContainer("stock_exchange:1.0-SNAPSHOT")
            .withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080)
    }

    private var users: Users? = null

    @Before
    fun before() {
        StocksClient("http://127.0.0.1", 8080).let {
            users = Users(it)
            var id = it.addCompany("MNST")!!
            it.addStocks(id, 100)
            it.changeCost(id, 100.0)
            id = it.addCompany("NVDA")!!
            it.addStocks(id, 1)
            it.changeCost(id, 10.0)
            id = it.addCompany("DMLRY")!!
            it.addStocks(id, 5)
            it.changeCost(id, 1000.0)
            users!!.addUser("TestUser").toLong()
        }
    }

    @Test
    @Throws(Exception::class)
    fun testStockMarket() {
        assertTrue(users!!.addMoney(0, 5.0))
        assertFalse(users!!.buyStock(0, 1, 1))
        assertTrue(users!!.addMoney(0, 15.0))
        assertTrue(users!!.buyStock(0, 1, 1))
        assertEquals(20.0, users!!.getUserMoney(0)!!, 0.0001)
        assertTrue(users!!.isStockSold(0, 1, 1))
    }
}
