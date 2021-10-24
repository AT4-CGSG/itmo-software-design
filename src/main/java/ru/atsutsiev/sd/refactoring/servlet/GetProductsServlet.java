package ru.atsutsiev.sd.refactoring.servlet;

import ru.atsutsiev.sd.refactoring.database.ProductsTable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * @author atsutsiev
 */
public class GetProductsServlet extends HttpServlet {
    private final ProductsTable database;

    public GetProductsServlet(final ProductsTable database) { this.database = database; }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            writer.println(database.queryAsHTML("all"));
        } catch (final SQLException e) {
            throw new RuntimeException("SQLException: " + e); // I'm sorry for this
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
