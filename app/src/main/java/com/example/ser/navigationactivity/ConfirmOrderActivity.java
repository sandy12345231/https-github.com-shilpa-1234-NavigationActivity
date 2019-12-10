package com.example.ser.navigationactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConfirmOrderActivity extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerview1;
    RecyclerView.Adapter arrayadapter;
    RecyclerView.LayoutManager layoutmanager;
    ArrayList<OrderPojo> arrayList = new ArrayList<>();
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        setTitle("My Order");
        recyclerview1 = (RecyclerView) findViewById(R.id.recyclerview);
        layoutmanager = new LinearLayoutManager(this);
        recyclerview1.setLayoutManager(layoutmanager);
        recyclerview1.setHasFixedSize(true);
        reference = FirebaseDatabase.getInstance().getReference("OrderInfo");

        SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        userId = sharedPreferences.getString("id", null);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderPojo orderPojo = snapshot.getValue(OrderPojo.class);
                    if (userId.equals(orderPojo.getUserId())) {
                        arrayList.add(orderPojo);
                    }
                }
                arrayadapter = new OrderAdapter(ConfirmOrderActivity.this, arrayList);
                recyclerview1.setAdapter(arrayadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });

    }
}
