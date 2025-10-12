package com.example.alexmexicanfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.alexmexicanfood.databinding.ActivityMenuBinding;

import java.util.List;

public class MenuActivity extends AppCompatActivity implements MenuItemAdapter.OnProductAddedListener {

    private ActivityMenuBinding binding;
    private DatabaseHelper dbHelper;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Menú de Venta");

        dbHelper = new DatabaseHelper(this);
        cart = Cart.getInstance();

        setupRecyclerView();

        binding.fabViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        List<Producto> productList = dbHelper.getAllProductos();
        MenuItemAdapter adapter = new MenuItemAdapter(productList, this);
        binding.recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMenu.setAdapter(adapter);
    }

    @Override
    public void onProductAdded(Producto producto) {
        cart.addItem(producto);
        Toast.makeText(this, producto.getNombre() + " agregado al carrito", Toast.LENGTH_SHORT).show();
    }
}
