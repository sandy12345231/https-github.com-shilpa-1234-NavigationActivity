package com.example.ser.navigationactivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddressSetActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView recyclerview2;
    String State[] = {"Rajasthan", "Uttar Pradesh", "Madhya Pradesh", "Punjab", "Maharashtra", "Gujrat", "Bihar", "Assam", "Andhra Pradesh", "Haryana"};
    String stateName;
    ArrayAdapter<String> arrayAdapter1;
    ArrayList<AddressPojo> addresslist = new ArrayList<>();
    AddressAdapter addressAdapter;
    AddressPojo addressPojo;
    RecyclerView.LayoutManager gridlayoutManager;
    String userId;
    SharedPreferences sharedPreferences;
    FloatingActionButton fabNext, fab;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        databaseReference = FirebaseDatabase.getInstance().getReference("Address");
        recyclerview2 = (RecyclerView) findViewById(R.id.recyclerview2);
        recyclerview2.setAdapter(addressAdapter);
        gridlayoutManager = new LinearLayoutManager(this);

        recyclerview2.setLayoutManager(gridlayoutManager);
        sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabNext = (FloatingActionButton) findViewById(R.id.fabNext);

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("1234", "onClick: ");
                Intent intent = new Intent(AddressSetActivity.this, PayToPaymentActivity.class);
                startActivity(intent);

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddressSetActivity.this);
                LayoutInflater layoutInflater = getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.add_address, null);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                final EditText flatno = (EditText) dialogView.findViewById(R.id.flatno);
                final EditText city = (EditText) dialogView.findViewById(R.id.city);
                final EditText area = (EditText) dialogView.findViewById(R.id.area);
                final EditText editName = (EditText) dialogView.findViewById(R.id.editName);
                final EditText editNumber = (EditText) dialogView.findViewById(R.id.editNumber);
                final EditText pincode = (EditText) dialogView.findViewById(R.id.pincode);
                final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinnerState);
                final Button buttoncontinue = (Button) dialogView.findViewById(R.id.buttoncontinue);
                arrayAdapter1 = new ArrayAdapter<String>(AddressSetActivity.this, android.R.layout.simple_spinner_item, State);
                spinner.setAdapter(arrayAdapter1);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        stateName = State[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                buttoncontinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AddressPojo addressPojo = new AddressPojo();
                        String Flat_No = flatno.getText().toString();
                        String name = editName.getText().toString();
                        String number = editNumber.getText().toString();
                        String City = city.getText().toString();
                        String Area = area.getText().toString();
                        String Pincode = pincode.getText().toString();


                        boolean flag;
                        if (Flat_No.equals("")) {
                            flatno.setError("Field Required");
                            flag = false;
                        } else {
                            flatno.setError(number);
                            flag = true;
                        }

                        if (name.equals("") && name.matches("[a-zA-Z]+\\\\.?")) {
                            editName.setError("Field Required");
                            flag = false;
                        } else {
                            editName.setError(name);
                            flag = true;
                        }

                        if (number.equals("") && number.length() < 10) {
                            editNumber.setError("Field Required");
                            flag = false;
                        } else {
                            editNumber.setError(number);
                            flag = true;
                        }

                        if (City.equals("")) {
                            city.setError("Field Required");
                            flag = false;
                        } else {
                            city.setError(number);
                            flag = true;
                        }

                        if (Pincode.length() > 6 && Pincode.length() < 6) {
                            pincode.setError("Field Required");
                            flag = false;
                        } else {
                            pincode.setError(number);
                            flag = true;
                        }


                        if (flag) {
                            String id = databaseReference.push().getKey();
                            addressPojo.setFlatNo(Flat_No);
                            addressPojo.setArea(Area);
                            addressPojo.setName(name);
                            addressPojo.setNumber(number);
                            addressPojo.setCity(City);
                            addressPojo.setPincode(Pincode);
                            addressPojo.setState(stateName);
                            addressPojo.setStatus("false");
                            addressPojo.setUserId(sharedPreferences.getString("id", null));
                            addressPojo.setAddId(id);
                            databaseReference.child(id).setValue(addressPojo);
                            Toast.makeText(getApplicationContext(), "Address Added!", Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);

        userId = sharedPreferences.getString("id", null);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addresslist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    addressPojo = snapshot.getValue(AddressPojo.class);
                    if (userId.equals(addressPojo.getUserId())) {
                        addresslist.add(addressPojo);
                    }

                    addressAdapter = new AddressAdapter(AddressSetActivity.this, addresslist);
                    recyclerview2.setAdapter(addressAdapter);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

   // public void callProceedToPayment() { fabNext.setShowMotionSpec();
    }

