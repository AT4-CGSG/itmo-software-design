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
class QueryServlet(model: Products) : ProductServlet(model) {
    @Throws(SQLException::class)
    override fun doGetInner(request: HttpServletRequest, response: HttpServletResponse) {
        response.writer.apply { print(model.queryAsHTML(request.getParameter("command"))) }
    }
}