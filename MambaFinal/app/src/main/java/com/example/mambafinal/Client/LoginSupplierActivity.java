package com.example.mambafinal.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mambafinal.R;

public class LoginSupplierActivity extends AppCompatActivity {

    private TextView NotSupplierLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_supplier);

        NotSupplierLink = findViewById(R.id.NotSupplierLink);

        NotSupplierLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginSupplierActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}