package com.example.alexmexicanfood;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.alexmexicanfood.databinding.ActivitySaleDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaleDetailActivity extends AppCompatActivity {

    private ActivitySaleDetailBinding binding;
    private DatabaseHelper dbHelper;
    private long saleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaleDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Detalle de Venta");

        dbHelper = new DatabaseHelper(this);
        saleId = getIntent().getLongExtra("SALE_ID", -1);

        if (saleId != -1) {
            loadSaleDetails();
        }
    }

    private void loadSaleDetails() {
        Sale sale = dbHelper.getSaleById(saleId);
        if (sale != null) {
            binding.detailSaleId.setText(String.format(Locale.getDefault(), "Venta #%d", sale.getId()));
            binding.detailSaleTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", sale.getTotalPrice()));

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            binding.detailSaleDate.setText(sdf.format(new Date(sale.getTimestamp())));

            SaleItemAdapter adapter = new SaleItemAdapter(sale.getItems());
            binding.recyclerViewSaleItems.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerViewSaleItems.setAdapter(adapter);
        }
    }
}
