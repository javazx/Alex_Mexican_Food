package com.example.alexmexicanfood;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.alexmexicanfood.databinding.ActivityCartBinding;

import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private Cart cart;
    private CartAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Carrito de Compras");

        cart = Cart.getInstance();
        dbHelper = new DatabaseHelper(this);

        setupRecyclerView();
        updateTotal();

        binding.btnFinalizeSale.setOnClickListener(v -> finalizeSale());
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(cart.getItems());
        binding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCart.setAdapter(adapter);
    }

    private void updateTotal() {
        binding.textTotalPrice.setText(String.format(Locale.getDefault(), "Total: $%.2f", cart.getTotalPrice()));
    }

    private void finalizeSale() {
        if (cart.getItems().isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Finalizar Venta")
                .setMessage("¿Confirmar la venta por un total de " + String.format(Locale.getDefault(), "$%.2f", cart.getTotalPrice()) + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    dbHelper.addSale(cart.getItems(), cart.getTotalPrice());
                    cart.clearCart();
                    Toast.makeText(this, "¡Venta realizada con éxito!", Toast.LENGTH_LONG).show();
                    finish(); // Close the cart and go back to the menu
                })
                .setNegativeButton("No", null)
                .show();
    }
}
