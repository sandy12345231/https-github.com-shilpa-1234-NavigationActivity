package com.example.ser.navigationactivity;

import android.app.ProgressDialog;
import android.content.Context;
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

public class AddToCartActivity extends AppCompatActivity {
    DatabaseReference reference;
    RecyclerView recyclerview1;
    RecyclerView.Adapter arrayadapter;
    RecyclerView.LayoutManager layoutmanager;
    CartPojo cartPojo;
    String userId;
    ArrayList<CartPojo> cartarray = new ArrayList<>();
    TextView textEmptycart;
    private int Quantity = 0;
    private int totalprice = 0;
    private int overallprice = 0;
    private int discount;
    private int Final_Price = 0;
    int totalQuantity = 0;
    int price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        setTitle("My Cart");
        recyclerview1 = (RecyclerView) findViewById(R.id.recyclerview1);
        layoutmanager = new LinearLayoutManager(this);
        recyclerview1.setLayoutManager(layoutmanager);
        recyclerview1.setHasFixedSize(true);

        reference = FirebaseDatabase.getInstance().getReference("Cart");
        final Button buttonproceedtoPay = (Button) findViewById(R.id.buttonproceedtoPay);
        textEmptycart = (TextView) findViewById(R.id.textEmptycart);


        SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        userId = sharedPreferences.getString("id", null);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartarray.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    cartPojo = snapshot.getValue(CartPojo.class);
                    if (userId.equals(cartPojo.getUserId())) {
                        Log.d("12345", "onDataChange: cartArray " + cartPojo.getQuantity());
                        cartarray.add(cartPojo);
                        Log.d("12345", "onDataChange: " + cartarray);
                    }
                }
                if (cartarray.size() > 0) {
                    recyclerview1.setVisibility(View.VISIBLE);
                    buttonproceedtoPay.setVisibility(View.VISIBLE);
                    textEmptycart.setVisibility(View.GONE);
                }
                arrayadapter = new CartAdapter(AddToCartActivity.this, cartarray);
                recyclerview1.setAdapter(arrayadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
        buttonproceedtoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < cartarray.size(); i++) {
                    Quantity = Integer.parseInt(cartarray.get(i).getQuantity());
                    price = Integer.parseInt(cartarray.get(i).getPrice());
                    totalprice = Quantity * price;
                    overallprice = overallprice + totalprice;
                    totalQuantity = totalQuantity + Quantity;
                }

                SharedPreferences sharedPreferences = getSharedPreferences("order", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("TotalPrice", String.valueOf(overallprice));
                editor.putString("TotalQuantity", String.valueOf(totalQuantity));
                editor.commit();

                Intent intent = new Intent(AddToCartActivity.this, AddressSetActivity.class);
                startActivity(intent);
            }
        });
    }

}
