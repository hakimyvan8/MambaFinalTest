package com.example.mambafinal.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mambafinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private TextView backBtn;
    private EditText InputName, InputPhoneNumber, InputPassword,TinNumber,BusinessEt;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.SignUp_register);
        InputName = (EditText) findViewById(R.id.SignUp_Name);
        backBtn = findViewById(R.id.backBtn);
        BusinessEt = findViewById(R.id.BusinessEt);
        TinNumber = findViewById(R.id.TinNumber);
        InputPhoneNumber = (EditText) findViewById(R.id.SignUp_number);
        InputPassword = (EditText) findViewById(R.id.SignUp_Password);
        loadingBar = new ProgressDialog(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void CreateAccount() {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        String Business = BusinessEt.getText().toString();
        String Tin = TinNumber.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Business))
        {
            Toast.makeText(this, "Please write the Business name...", Toast.LENGTH_SHORT).show();
        }
        else if (!Tin.matches("^[0-1][2][0-9]{7}$"))
        {
            Toast.makeText(this, "Invalid Tin Number...", Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Validatephone(name, phone, password,Business,Tin);
        }
    }

    private void Validatephone(final String name, final String phone, final String password,final String Business, final String Tin)
    {
        final DatabaseReference myRef;
        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (!(snapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("Business",Business);
                    userdataMap.put("Tin",Tin);
                    userdataMap.put("name",name);
                    userdataMap.put("Password",password);

                    myRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Congratulations, Your Account has been Created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please Try Again...", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();


                                    }

                                }
                            });

                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This" + phone + "already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please Try Another phone", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
