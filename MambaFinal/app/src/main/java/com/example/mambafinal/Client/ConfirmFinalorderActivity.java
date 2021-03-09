package com.example.mambafinal.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mambafinal.Prevalent.Prevalent;
import com.example.mambafinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalorderActivity extends AppCompatActivity {

    private EditText PersonName, LocationPhoneNumber, LocationHomeAddress, LocationCityName;
    private Button confirmOrder;
    private ImageButton backBtn;
    private int totalPrice = 0;
    AlertDialog.Builder alertBuilder;
    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_finalorder);
        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price = Rwf " + totalAmount, Toast.LENGTH_SHORT).show();

        confirmOrder = (Button) findViewById(R.id.confirm_location);
        backBtn = findViewById(R.id.backBtn);
        PersonName = (EditText) findViewById(R.id.shipment_name);
        LocationPhoneNumber = (EditText) findViewById(R.id.shipment_phone_number);
        LocationHomeAddress = (EditText) findViewById(R.id.shipment_address);
        LocationCityName = (EditText) findViewById(R.id.shipment_city);

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Check();

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void Check()
    {
        if (TextUtils.isEmpty(PersonName.getText().toString()))
        {
            PersonName.setError( "Please provide your Full Name");
        }
        else if (TextUtils.isEmpty(LocationPhoneNumber.getText().toString()))
        {
           LocationPhoneNumber.setError( "Please provide your phone number");
        }
        else if (TextUtils.isEmpty(LocationHomeAddress.getText().toString()))
        {
            LocationHomeAddress.setError("Please provide your Address");
        }
        else if (TextUtils.isEmpty(LocationCityName.getText().toString()))
        {
            LocationCityName.setError("Please provide your City Name");
        }
        else
        {
            ConfirmOrder();
        }
    }



    private void ConfirmOrder()
    {
        final String saveCurrentDate,saveCurrentTime;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name",PersonName.getText().toString());
        ordersMap.put("phone",LocationPhoneNumber.getText().toString());
        ordersMap.put("address",LocationHomeAddress.getText().toString());
        ordersMap.put("city",LocationCityName.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ConfirmFinalorderActivity.this);
                                        alertBuilder.setTitle("Confirm before purchase");
                                        alertBuilder.setMessage("amount:"+totalAmount);

                                        alertBuilder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                Toast.makeText(ConfirmFinalorderActivity.this, "your final order has been placed successfully.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ConfirmFinalorderActivity.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });
                                        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                dialog.dismiss();
                                            }
                                        });
                                        alertBuilder.show();
                                    }
                                }
                            });

                }
            }
        });
    }
}