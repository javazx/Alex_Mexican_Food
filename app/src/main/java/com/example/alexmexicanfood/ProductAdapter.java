package com.example.alexmexicanfood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alexmexicanfood.databinding.ItemProductBinding;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Producto> productList;
    private final OnProductListener listener;

    public interface OnProductListener {
        void onEditClick(Producto producto);
        void onDeleteClick(Producto producto);
    }

    public ProductAdapter(List<Producto> productList, OnProductListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemProductBinding binding = ItemProductBinding.inflate(layoutInflater, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Producto producto = productList.get(position);
        holder.bind(producto, listener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProducts(List<Producto> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        public ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Producto producto, final OnProductListener listener) {
            binding.textProductName.setText(producto.getNombre());
            binding.textProductDescription.setText(producto.getDescripcion());
            binding.textProductPrice.setText(String.format(Locale.getDefault(), "$%.2f", producto.getPrecio()));

            binding.btnEditProduct.setOnClickListener(v -> listener.onEditClick(producto));
            binding.btnDeleteProduct.setOnClickListener(v -> listener.onDeleteClick(producto));
        }
    }
}
