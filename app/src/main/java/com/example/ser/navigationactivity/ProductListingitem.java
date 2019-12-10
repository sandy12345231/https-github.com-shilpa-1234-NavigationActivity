package com.example.ser.navigationactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.R.attr.key;
import static android.R.attr.password;
import static com.example.ser.navigationactivity.R.drawable.user;
import static com.example.ser.navigationactivity.R.id.addCart;
import static com.example.ser.navigationactivity.R.id.email;
import static com.example.ser.navigationactivity.R.id.image;
import static com.example.ser.navigationactivity.R.id.parent;

public class ProductListingitem extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView recycler;
    CustomAdapter adapter;
    GridLayoutManager gridlayoutManager;
    TextView price, brandname;
    Integer quantity;
    Button addCart, buttonAdd;
    ImageView additem, removeitem, imageview1;
    ProductPojo productPojo;
    ArrayList<ProductPojo> arrayList = new ArrayList<>();
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);
        additem = (ImageView) findViewById(R.id.add);
        addCart = (Button) findViewById(R.id.addCart);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        removeitem = (ImageView) findViewById(R.id.removeitem);
        recycler = (RecyclerView) findViewById(R.id.recyclerview);
        recycler.setHasFixedSize(true);
        price = (TextView) findViewById(R.id.price);
        brandname = (TextView) findViewById(R.id.brandname);
        databaseReference = FirebaseDatabase.getInstance().getReference("product");
        gridlayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recycler.setLayoutManager(gridlayoutManager);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        setTitle(category);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    productPojo = snapshot.getValue(ProductPojo.class);
                    if (category.equals(productPojo.getCategory()))
                        arrayList.add(productPojo);
                }
                adapter = new CustomAdapter(ProductListingitem.this, arrayList);
                recycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }


}


