package org.at4software.software_design

import org.springframework.http.HttpStatus
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

/**
 * @author Tsutsiev Andrey
 */
class StocksClient(private val host: String, private val port: Int) {
    private var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build()
    private val url = "$host:$port"

    fun buyStocks(companyId: Int, amount: Int): Boolean =
        makeRequest("$url/buy_stock?id=$companyId&amount=$amount")?.toBoolean() ?: false

    fun getStockPrice(companyId: Int): Double? =
        makeRequest("$url/get_stock_price?id=$companyId")?.toDouble()

    fun getStockAmount(companyId: Int): Int? =
        makeRequest("$url/get_stock_amount?id=$companyId")?.toInt()

    fun addCompany(name: String): Int? =
        makeRequest("$url/add_company?name=$name")?.toInt()

    fun changeCost(id: Int, delta: Double) {
        makeRequest("$url/change_stock_cost?id=$id&delta=$delta")
    }

    fun addStocks(id: Int, amount: Int) {
        makeRequest("$url/add_stocks?id=$id&amount=$amount")
    }

    fun sellStocks(companyId: Int, amount: Int): Boolean =
        makeRequest("$url/sell_stock?id=$companyId&amount=$amount")?.toBoolean() ?: false

    private fun makeRequest(requestURI: String): String? {
        try {
            client.send(
                HttpRequest.newBuilder().uri(URI(requestURI)).GET().build(),
                HttpResponse.BodyHandlers.ofString())
                .apply { if (statusCode() == HttpStatus.OK.value()) return@makeRequest body() }
        } catch (e: URISyntaxException) { e.printStackTrace() }
        catch (e: InterruptedException) { e.printStackTrace() }
        catch (e: IOException) { e.printStackTrace() }
        return null
    }
}