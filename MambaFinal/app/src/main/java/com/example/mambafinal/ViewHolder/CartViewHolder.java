package com.example.mambafinal.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mambafinal.Interface.ItemClickListener;
import com.example.mambafinal.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView itemProductName,itemProductPrice,itemProductQuality;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);

        itemProductName = itemView.findViewById(R.id.cart_product_name);
        itemProductPrice = itemView.findViewById(R.id.cart_product_price);
        itemProductQuality = itemView.findViewById(R.id.cart_product_quantity);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
}
