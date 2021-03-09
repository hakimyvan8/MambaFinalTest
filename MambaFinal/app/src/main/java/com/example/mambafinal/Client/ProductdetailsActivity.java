package com.example.mambafinal.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mambafinal.Model.Products;
import com.example.mambafinal.Prevalent.Prevalent;
import com.example.mambafinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductdetailsActivity extends AppCompatActivity {

    private Button ProceedToCart;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private ImageButton backBtn;
    private TextView productPrice, productQuality,productName,productDescription;
    private String productID = "", state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);

        productID = getIntent().getStringExtra("pid");

        ProceedToCart = (Button) findViewById(R.id.details_add_cart);
        productImage = (ImageView) findViewById(R.id.products_image_details);
        backBtn = findViewById(R.id.backBtn);
        numberButton = (ElegantNumberButton) findViewById(R.id.details_elegant);
        productPrice = (TextView) findViewById(R.id.details_price);
        productQuality = (TextView) findViewById(R.id.details_quantity);
        productName = (TextView) findViewById(R.id.details_product_name);
        productDescription = (TextView) findViewById(R.id.details_description);


        getProductDetails(productID);

        ProceedToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (state.equals("Orders Placed") || state.equals("Orders shipped"))
                {
                    Toast.makeText(ProductdetailsActivity.this, "Purchase More Products, when Orders are confirmed or pending", Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        CheckOrderState();
    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(callForDate.getTime());

       final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("quantity number",productQuality.getText().toString());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            Toast.makeText(ProductdetailsActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(ProductdetailsActivity.this, HomeActivity.class);
                                            startActivity(intent);

                                        }
                                    });
                        }
                    }
                });

    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    Products products = snapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productQuality.setText(products.getQuantity());
                    productDescription.setText(products.getDescription());

                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String shippingState = snapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                      state = "Orders shipped";
                    }
                    else if (shippingState.equals("not shipped"))
                    {
                        state = "Orders Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}