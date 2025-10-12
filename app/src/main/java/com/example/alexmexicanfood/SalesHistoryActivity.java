package com.example.alexmexicanfood;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.alexmexicanfood.databinding.ActivitySalesHistoryBinding;

import java.util.ArrayList;

public class SalesHistoryActivity extends AppCompatActivity implements SalesHistoryAdapter.OnSaleClickListener {

    private ActivitySalesHistoryBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySalesHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Historial de Ventas");

        dbHelper = new DatabaseHelper(this);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        ArrayList<Sale> salesList = (ArrayList<Sale>) dbHelper.getAllSales();
        SalesHistoryAdapter adapter = new SalesHistoryAdapter(salesList, this);
        binding.recyclerViewSalesHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewSalesHistory.setAdapter(adapter);
    }

    @Override
    public void onSaleClick(Sale sale) {
        Intent intent = new Intent(this, SaleDetailActivity.class);
        intent.putExtra("SALE_ID", sale.getId());
        startActivity(intent);
    }
}
