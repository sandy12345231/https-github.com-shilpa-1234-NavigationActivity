package com.example.ser.navigationactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PayToPaymentActivity extends AppCompatActivity {

    DatabaseReference databaseReference, databaseReference1;
    TextView editTotal, editQuant, editdiscount, editTotalamount;
    Button buttonOrderNow;
    SharedPreferences sharedPreferences, sharedPreferences1;
    String userId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_to_payment);

        editdiscount = (TextView) findViewById(R.id.editdiscount);
        editQuant = (TextView) findViewById(R.id.editQuant);
        editTotal = (TextView) findViewById(R.id.editTotal);
        editTotalamount = (TextView) findViewById(R.id.editTotalamount);
        buttonOrderNow = (Button) findViewById(R.id.buttonOrderNow);
        databaseReference = FirebaseDatabase.getInstance().getReference("OrderInfo");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Cart");
        sharedPreferences = getSharedPreferences("order", MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences("Data", MODE_PRIVATE);
        userId = sharedPreferences1.getString("id", null);
        Log.d("1234", "onCreate: "+userId);
        String priceTotal = sharedPreferences.getString("TotalPrice", null);
        String quantityTotal = sharedPreferences.getString("TotalQuantity", null);
        editTotal.setText(priceTotal);
        editQuant.setText(quantityTotal);
        editTotalamount.setText(priceTotal);
        buttonOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String total = editTotal.getText().toString();
                String quantity = editQuant.getText().toString();
                String discount = editdiscount.getText().toString();
                String paybleAmount = editTotalamount.getText().toString();
                String orderId = databaseReference.push().getKey();
                OrderPojo pojo = new OrderPojo();
                pojo.setDiscount(discount);
                pojo.setOrderId(orderId);
                pojo.setPayableAmount(paybleAmount);
                pojo.setQuantity(quantity);
                pojo.setTotalPrice(total);
                pojo.setUserId(userId);
                pojo.setStatus("Pending");
                databaseReference.child(orderId).setValue(pojo);
                confirmOrder();

            }
        });


    }

    private void confirmOrder() {
        Log.d("1234", "confirmOrder: ");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    CartPojo pojo = data.getValue(CartPojo.class);
                    Log.d("1234", "onDataChange: "+userId+pojo.getId());
                    if (pojo.getUserId().equals(userId)) {
                        databaseReference1.child(pojo.getId()).removeValue();
                        Log.d("1234", "onDataChange:12 ");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        AlertDialog.Builder dialog = new AlertDialog.Builder(PayToPaymentActivity.this);
        dialog.setTitle("Confirmation");
        dialog.setMessage("Hello " + sharedPreferences1.getString("name", null) + ", Your Order has been confirm. Your item will be dispatched shortly");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PayToPaymentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();


    }
}
