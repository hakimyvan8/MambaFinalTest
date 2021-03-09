package com.example.mambafinal.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mambafinal.Model.Users;
import com.example.mambafinal.Prevalent.Prevalent;
import com.example.mambafinal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber,InputPassword;
    private Button loginbutton;
    private ProgressDialog loadingBar;
    private TextView SupplierLink,forgotPassword,backBtn;
    private String parentDbName = "Users";
    private CheckBox chkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbutton = (Button) findViewById(R.id.login_button);
        InputPhoneNumber = (EditText) findViewById(R.id.login_number);
        InputPassword = (EditText) findViewById(R.id.login_Password);
        backBtn = findViewById(R.id.backBtn);
        forgotPassword = (TextView) findViewById(R.id.login_forgotPassword);
        SupplierLink = (TextView) findViewById(R.id.SupplierLink);
        loadingBar = new ProgressDialog(this);

        chkBox = (CheckBox) findViewById(R.id.login_checkBox);
        Paper.init(this);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginUsers();

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SupplierLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, LoginSupplierActivity.class);
                startActivity(intent);
            }
        });
    }



    private void LoginUsers()
    {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(InputPhoneNumber.getText().toString()))
       {
        InputPhoneNumber.setError("Please write valid phone number");
        } else if (TextUtils.isEmpty(InputPassword.getText().toString()))
         {
        InputPassword.setError("Please write valid Password");
         }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        if (chkBox.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

         final DatabaseReference myRef;
        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child(parentDbName).child(phone).exists())
                {
                    Users userData = snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (userData.getPhone().equals(phone))
                    {
                        if (userData.getPassword().equals(password))
                        {
                         if (parentDbName.equals("Suppliers"))
                         {
                             Toast.makeText(LoginActivity.this, "Welcome Admin, You are Logged in Successfully", Toast.LENGTH_SHORT).show();
                             loadingBar.dismiss();

                             Intent intent = new Intent(LoginActivity.this, HomeSupplierActivity.class);
                             Prevalent.currentOnlineUser = userData;
                             startActivity(intent);
                         }
                         else if (parentDbName.equals("Users"))
                         {
                             Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                             loadingBar.dismiss();

                             Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                             Prevalent.currentOnlineUser = userData;
                             startActivity(intent);
                         }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this" + phone + "number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}