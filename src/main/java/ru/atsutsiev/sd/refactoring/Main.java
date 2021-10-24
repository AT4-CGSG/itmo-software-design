package ru.atsutsiev.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.atsutsiev.sd.refactoring.database.ProductsTable;
import ru.atsutsiev.sd.refactoring.servlet.AddProductServlet;
import ru.atsutsiev.sd.refactoring.servlet.GetProductsServlet;
import ru.atsutsiev.sd.refactoring.servlet.QueryServlet;

/**
 * @author atsutsiev
 */
public class Main {
    public static void main(String[] args) throws Exception {
        final ProductsTable products = new ProductsTable("jdbc:sqlite:test.db");

        products.createTable();

        final Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(products)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(products)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(products)),"/query");

        server.start();
        server.join();
    }
}
