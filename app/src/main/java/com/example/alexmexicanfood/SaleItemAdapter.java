package com.example.alexmexicanfood;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alexmexicanfood.databinding.ItemCartBinding; // Reusing the same layout

import java.util.List;
import java.util.Locale;

public class SaleItemAdapter extends RecyclerView.Adapter<SaleItemAdapter.SaleItemViewHolder> {

    private final List<SaleItem> saleItems;

    public SaleItemAdapter(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }

    @NonNull
    @Override
    public SaleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // We can reuse the item_cart layout as it's structurally similar
        ItemCartBinding binding = ItemCartBinding.inflate(inflater, parent, false);
        return new SaleItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleItemViewHolder holder, int position) {
        SaleItem item = saleItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return saleItems.size();
    }

    static class SaleItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartBinding binding;

        public SaleItemViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SaleItem item) {
            binding.textProductName.setText(item.getProductName());
            binding.textProductPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));
        }
    }
}
