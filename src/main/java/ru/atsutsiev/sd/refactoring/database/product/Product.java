package ru.atsutsiev.sd.refactoring.database.product;

import java.util.Objects;

/**
 * @author atsutsiev
 */
public class Product {
    private final String name;
    private final long price;

    public Product(final String name, final long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }

    public long getPrice() { return price; }

    public String toHTML() { return String.format("%s\t%d</br>", name, price); }

    @Override
    public String toString() { return String.format("Product {  name: %s price: %d}", name, price); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() { return Objects.hash(name, price); }
}
