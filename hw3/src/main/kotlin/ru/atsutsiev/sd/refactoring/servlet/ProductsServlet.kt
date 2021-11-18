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
abstract class ProductsServlet(@Suppress("unused") protected val model: Products) : HttpServlet() {
    @Throws(SQLException::class)
    protected abstract fun doGetInner(request: HttpServletRequest, response: HttpServletResponse)

    @Throws(IOException::class)
    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        try { doGetInner(request, response) }
        catch (e: SQLException) { throw RuntimeException("SQLException: $e") } // I'm sorry for this

        response.apply {
            contentType = "text/html"
            status = HttpServletResponse.SC_OK
        }
    }
}
