package com.example.alexmexicanfood;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alexmexicanfood.databinding.ItemMenuProductBinding;

import java.util.List;
import java.util.Locale;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    private final List<Producto> productList;
    private final OnProductAddedListener listener;

    public interface OnProductAddedListener {
        void onProductAdded(Producto producto);
    }

    public MenuItemAdapter(List<Producto> productList, OnProductAddedListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMenuProductBinding binding = ItemMenuProductBinding.inflate(layoutInflater, parent, false);
        return new MenuItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        Producto producto = productList.get(position);
        holder.bind(producto, listener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemMenuProductBinding binding;

        public MenuItemViewHolder(ItemMenuProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Producto producto, final OnProductAddedListener listener) {
            binding.textProductName.setText(producto.getNombre());
            binding.textProductDescription.setText(producto.getDescripcion());
            binding.textProductPrice.setText(String.format(Locale.getDefault(), "$%.2f", producto.getPrecio()));

            binding.btnAddToCart.setOnClickListener(v -> listener.onProductAdded(producto));
        }
    }
}
