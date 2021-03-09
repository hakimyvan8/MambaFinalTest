package com.example.mambafinal.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mambafinal.Interface.ItemClickListener;
import com.example.mambafinal.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductDescription;
    public ImageView imageView,next;
    public ItemClickListener listener;
    public ProductViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.item_image);
        txtProductName = (TextView) itemView.findViewById(R.id.item_product_name);
        txtProductDescription= (TextView) itemView.findViewById(R.id.item_Description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.item_price);
        next = itemView.findViewById(R.id.next);
    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v)
    {
        listener.onClick(v,getAdapterPosition(),false);

    }
}
