package com.example.alexmexicanfood;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alexmexicanfood.databinding.ActivityProductFormBinding;

public class ProductFormActivity extends AppCompatActivity {

    private ActivityProductFormBinding binding;
    private DatabaseHelper dbHelper;
    private long productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        if (getIntent().hasExtra("PRODUCT_ID")) {
            productId = getIntent().getLongExtra("PRODUCT_ID", -1);
            loadProductData();
            setTitle("Editar Producto");
        } else {
            setTitle("Agregar Producto");
        }

        binding.btnSaveProduct.setOnClickListener(v -> saveProduct());
    }

    private void loadProductData() {
        // This is not efficient, but for simplicity, we query all and find the one.
        // A better approach would be a dbHelper.getProductById(id) method.
        for (Producto p : dbHelper.getAllProductos()) {
            if (p.getId() == productId) {
                binding.editTextProductName.setText(p.getNombre());
                binding.editTextProductDescription.setText(p.getDescripcion());
                binding.editTextProductPrice.setText(String.valueOf(p.getPrecio()));
                break;
            }
        }
    }

    private void saveProduct() {
        String name = binding.editTextProductName.getText().toString().trim();
        String description = binding.editTextProductDescription.getText().toString().trim();
        String priceStr = binding.editTextProductPrice.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "El nombre y el precio son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        Producto producto = new Producto();
        producto.setNombre(name);
        producto.setDescripcion(description);
        producto.setPrecio(price);

        if (productId == -1) {
            dbHelper.addProducto(producto);
            Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show();
        } else {
            producto.setId(productId);
            dbHelper.updateProducto(producto);
            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
        }

        finish(); // Go back to the previous activity
    }
}
