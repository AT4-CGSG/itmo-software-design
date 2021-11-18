package ru.atsutsiev.sd.refactoring

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import ru.atsutsiev.sd.refactoring.database.models.Products
import ru.atsutsiev.sd.refactoring.servlet.AddProductServlet
import ru.atsutsiev.sd.refactoring.servlet.GetProductsServlet
import ru.atsutsiev.sd.refactoring.servlet.QueryServlet

/**
 * @author atsutsuiev
 */
object Main {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val products = Products()
        products.create()

        val server = Server(8081)

        ServletContextHandler(ServletContextHandler.SESSIONS).apply {
            contextPath = "/"
            server.handler = this
            addServlet(ServletHolder(AddProductServlet(products)), "/add-product")
            addServlet(ServletHolder(GetProductsServlet(products)), "/get-products")
            addServlet(ServletHolder(QueryServlet(products)), "/query")
        }

        server.run {
            start()
            join()
        }
    }
}
