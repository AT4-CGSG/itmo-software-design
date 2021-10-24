package ru.atsutsiev.sd.refactoring.servlet;

import ru.atsutsiev.sd.refactoring.database.ProductsTable;
import ru.atsutsiev.sd.refactoring.database.product.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author atsutsiev
 */
public class AddProductServlet extends HttpServlet {
    private final ProductsTable database;

    public AddProductServlet(final ProductsTable database) { this.database = database; }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            database.insert(new Product(request.getParameter("name"), Long.parseLong(request.getParameter("price"))));
        } catch (final SQLException e) {
            throw new RuntimeException("SQLException: " + e); // I'm sorry for this
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}
