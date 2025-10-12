package com.example.alexmexicanfood;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alexmexicanfood.databinding.ItemSaleBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalesHistoryAdapter extends RecyclerView.Adapter<SalesHistoryAdapter.SaleViewHolder> {

    private final List<Sale> salesList;
    private final OnSaleClickListener listener;

    public interface OnSaleClickListener {
        void onSaleClick(Sale sale);
    }

    public SalesHistoryAdapter(List<Sale> salesList, OnSaleClickListener listener) {
        this.salesList = salesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSaleBinding binding = ItemSaleBinding.inflate(inflater, parent, false);
        return new SaleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleViewHolder holder, int position) {
        Sale sale = salesList.get(position);
        holder.bind(sale, listener);
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    static class SaleViewHolder extends RecyclerView.ViewHolder {
        private final ItemSaleBinding binding;

        public SaleViewHolder(ItemSaleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Sale sale, final OnSaleClickListener listener) {
            binding.textSaleId.setText(String.format(Locale.getDefault(), "Venta #%d", sale.getId()));
            binding.textSaleTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", sale.getTotalPrice()));

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            binding.textSaleDate.setText(sdf.format(new Date(sale.getTimestamp())));

            itemView.setOnClickListener(v -> listener.onSaleClick(sale));
        }
    }
}
