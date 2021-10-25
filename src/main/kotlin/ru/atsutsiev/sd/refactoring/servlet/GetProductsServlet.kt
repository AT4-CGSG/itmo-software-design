package ru.atsutsiev.sd.refactoring.servlet

import ru.atsutsiev.sd.refactoring.database.models.Products
import java.io.IOException
import java.sql.SQLException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author atsutsiev
 */
class GetProductsServlet(private val model: Products) : HttpServlet() {
    @Throws(IOException::class)
    override fun doGet(request: HttpServletRequest?, response: HttpServletResponse) {
        try {
            response.writer.apply { print(model.queryAsHTML("all")) }
        } catch (e: SQLException) {
            throw RuntimeException("SQLException: $e") // I'm sorry for this
        }

        response.apply {
            contentType = "text/html"
            status = HttpServletResponse.SC_OK
        }
    }
}