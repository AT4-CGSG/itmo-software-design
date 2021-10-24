package ru.atsutsiev.sd.refactoring.database;

import org.jetbrains.annotations.NotNull;
import ru.atsutsiev.sd.refactoring.database.product.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

/**
 * @author atsutsiev
 */
public class ProductsTable {
    private static final String TABLE_NAME = "PRODUCT";
    private static final String AVAILABLE_QUERIES = "max, min, sum, count, all";
    private final String databaseURL;

    public ProductsTable(final String databaseURL) {
        this.databaseURL = databaseURL;
    }

    public String queryAsHTML(final String queryCommand) throws IllegalArgumentException, SQLException {
        switch (queryCommand) {
            case "all"  : return toHTML(all());
            case "sum"  : return toHTML("Summary price: ", sum());
            case "count": return toHTML("Number of products: ", count());
            case "max"  : {
                final String header = "Product with max price: ";
                return max().map(p -> toHTML(header, p)).orElseGet(() -> toHTML(header));
            }
            case "min"  : {
                final String header = "Product with min price: ";
                return min().map(p -> toHTML(header, p)).orElseGet(() -> toHTML(header));
            }
            default     : return "Products table: available queries: " + AVAILABLE_QUERIES;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public int createTable() throws SQLException {
        try (final Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            try (Statement stmt = c.createStatement()) {
                return stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS PRODUCT" +
                            "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            " NAME           TEXT    NOT NULL, " +
                            " PRICE          INT     NOT NULL)"
                );
            }
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public int insert(@NotNull final Product product) throws SQLException {
        return executeUpdate(
                "INSERT INTO " + TABLE_NAME + " " +
                "(NAME, PRICE) VALUES " +
                "(\"" + product.getName() + "\"," + product.getPrice() + ")"
        );
    }

    public Connection getConnection() throws SQLException { return DriverManager.getConnection(databaseURL); }

    /* Queries */

    public List<Product> all() throws SQLException {
        return parseQueryOutput(executeQuery("SELECT * FROM " + TABLE_NAME));
    }

    public Optional<Product> max() throws SQLException {
        return parseQueryOutput(executeQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY PRICE DESC LIMIT 1"))
                .stream()
                .findFirst();
    }

    public Optional<Product> min() throws SQLException {
        return parseQueryOutput(executeQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY PRICE LIMIT 1"))
                .stream()
                .findFirst();
    }

    public long sum() throws SQLException {
        return executeQuery("SELECT SUM(price) as sum FROM " + TABLE_NAME).getLong("sum");
    }

    public int count() throws SQLException {
        return executeQuery("SELECT COUNT(*) as cnt FROM " + TABLE_NAME).getInt("cnt");
    }

    /* Internals */

    private ResultSet executeQuery(final String sql) throws SQLException {
        try (final Statement stmt = getConnection().createStatement()) { return stmt.executeQuery(sql); }
    }

    private int executeUpdate(final String sql) throws SQLException {
        try (final Statement stmt = getConnection().createStatement()) { return stmt.executeUpdate(sql); }
    }

    private List<Product> parseQueryOutput(ResultSet rs) throws SQLException {
        final List<Product> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new Product(rs.getString("name"), rs.getInt("price")));
        }
        return result;
    }

    private static String toHTML(final List<Product> products) {
        return "<html><body>\n" +
                products.stream().map(Product::toHTML).collect(joining("\n")) +
                "</body></html>\n";
    }

    private static String toHTML(final String header, @NotNull final Product product) {
        return "<html><body>\n" +
                "<h1>" + header + "</h1>\n" +
                product.toHTML() + "\n" +
                "</body></html>";
    }

    private static String toHTML(final String header, final Object info) {
        return String.format("<html><body>\n%s\n%s\n</body></html>", header, info);
    }

    private static String toHTML(final String header) {
        return "<html><body>\n" +
                "<h1>" + header + "</h1>\n" +
                "</body></html>";
    }
}
