package com.example.alexmexicanfood;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance;
    private final List<Producto> items;

    private Cart() {
        items = new ArrayList<>();
    }

    public static synchronized Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addItem(Producto producto) {
        // For simplicity, we add the product. A real app would handle quantities.
        items.add(producto);
    }

    public List<Producto> getItems() {
        return items;
    }

    public double getTotalPrice() {
        double total = 0;
        for (Producto producto : items) {
            total += producto.getPrecio();
        }
        return total;
    }

    public void clearCart() {
        items.clear();
    }
}
