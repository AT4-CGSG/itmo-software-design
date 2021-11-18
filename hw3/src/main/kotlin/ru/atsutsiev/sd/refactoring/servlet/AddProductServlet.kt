package ru.atsutsiev.sd.refactoring.servlet

import ru.atsutsiev.sd.refactoring.database.models.Products
import ru.atsutsiev.sd.refactoring.database.models.custom_fields.ProductDataRecord
import java.sql.SQLException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author atsutsiev
 */
class AddProductServlet(model: Products) : ProductsServlet(model) {
    @Throws(SQLException::class)
    override fun doGetInner(request: HttpServletRequest, response: HttpServletResponse) {
        request.apply { model.insert(ProductDataRecord(getParameter("name"), getParameter("price").toLong())) }
        response.writer.apply { println("OK") }
    }
}
