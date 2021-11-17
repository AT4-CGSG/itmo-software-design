package ru.atsutsiev.sd.refactoring.servlet

import ru.atsutsiev.sd.refactoring.database.models.Products
import ru.atsutsiev.sd.refactoring.database.models.custom_fields.ProductDataRecord
import java.io.IOException
import java.sql.SQLException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author atsutsiev
 */
class AddProductServlet(private val model: Products) : HttpServlet() {
    @Throws(IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        request.apply {
            try {
                model.insert(ProductDataRecord(getParameter("name"), getParameter("price").toLong()))
            } catch (e: SQLException) {
                throw RuntimeException("SQLException: $e") // I'm sorry for this
            }
        }

        response.apply {
            contentType = "text/html"
            status = HttpServletResponse.SC_OK
            writer.println("OK")
        }
    }
}