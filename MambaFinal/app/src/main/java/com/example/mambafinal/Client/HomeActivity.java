package com.example.mambafinal.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mambafinal.Model.Products;
import com.example.mambafinal.Prevalent.Prevalent;
import com.example.mambafinal.R;
import com.example.mambafinal.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private ImageView nav_editprofile;
    private String type = "",pid;
    private TextView searchBtn,cartCountTv,user_profile_name,phoneTv,nav_settings,nav_logout,tabProductTv,tabOrdersTv;
    private ImageButton cartBtn;
    private RelativeLayout shopsRL,ordersRL;
    private RecyclerView recyclerView,ordersRv;
    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Paper.init(this);
        user_profile_name = findViewById(R.id.user_profile_name);
        phoneTv = findViewById(R.id.phoneTv);
        cartBtn = findViewById(R.id.cartBtn);
        tabProductTv = findViewById(R.id.tabProductTv);
        tabOrdersTv = findViewById(R.id.tabOrdersTv);
        //get uid of the shop from intent
        pid = getIntent().getStringExtra("pid");
        recyclerView = findViewById(R.id.productRv);
        recyclerView.setHasFixedSize(true);
        shopsRL = findViewById(R.id.shopsRL);
        ordersRL = findViewById(R.id.ordersRL);
        ordersRv = findViewById(R.id.ordersRv);
        cartCountTv = findViewById(R.id.cartCountTv);
        profileImage = findViewById(R.id.user_profile_image);
        nav_editprofile = findViewById(R.id.nav_editprofile);
        nav_settings = findViewById(R.id.nav_settings);
        nav_logout = findViewById(R.id.nav_logout);

        nav_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        checkUser();

        nav_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });
        tabProductTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //show shops
                showShopsUI();
            }
        });
        tabOrdersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showOrdersUI();
            }
        });
    }

    private void showOrdersUI()
    {

        //show orders ui, hide shops ui
        shopsRL.setVisibility(View.GONE);
        ordersRL.setVisibility(View.VISIBLE);

        tabOrdersTv.setTextColor(getResources().getColor(R.color.white));
        tabOrdersTv.setBackgroundResource(R.drawable.shape_rect05);

        tabProductTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabProductTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void checkUser()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(Prevalent.currentOnlineUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String phone = ""+ds.child("phone").getValue();

                            //set user data
                            user_profile_name.setText(name);
                            phoneTv.setText(phone);
                            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.ic_person).into(profileImage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() + "RWF");
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                    Intent intent = new Intent(HomeActivity.this, ProductdetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout,parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;

                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void showShopsUI()
    {
        //show shops ui, hide orders ui
        shopsRL.setVisibility(View.VISIBLE);
        ordersRL.setVisibility(View.GONE);

        tabProductTv.setTextColor(getResources().getColor(R.color.white));
        tabProductTv.setBackgroundResource(R.drawable.shape_rect05);


        tabOrdersTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabOrdersTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }


    private void logout()
    {
        Paper.book().destroy();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}