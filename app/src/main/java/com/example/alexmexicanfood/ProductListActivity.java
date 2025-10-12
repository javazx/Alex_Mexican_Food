package com.example.alexmexicanfood;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.alexmexicanfood.databinding.ActivityProductListBinding;

import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductAdapter.OnProductListener {

    private ActivityProductListBinding binding;
    private DatabaseHelper dbHelper;
    private ProductAdapter adapter;
    private List<Producto> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        setupRecyclerView();

        binding.fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, ProductFormActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void setupRecyclerView() {
        productList = dbHelper.getAllProductos();
        adapter = new ProductAdapter(productList, this);
        binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewProducts.setAdapter(adapter);
    }

    private void loadProducts() {
        productList = dbHelper.getAllProductos();
        adapter.setProducts(productList);
    }

    @Override
    public void onEditClick(Producto producto) {
        Intent intent = new Intent(this, ProductFormActivity.class);
        intent.putExtra("PRODUCT_ID", producto.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Producto producto) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Producto")
                .setMessage("¿Estás seguro de que quieres eliminar este producto?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    dbHelper.deleteProducto(producto.getId());
                    loadProducts();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
