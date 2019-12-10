package com.example.ser.navigationactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class LoginActivity extends AppCompatActivity {


    EditText editTextEmail, editPass;
    DatabaseReference databaseReference;
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setActionBar("Login Activity");
        SharedPreferences preferences = getSharedPreferences("Data", MODE_PRIVATE);
        if (preferences.getBoolean("LoginStatus", false)) {
            String mail = preferences.getString("Email", null);
            String pass = preferences.getString("Password", null);
            if (mail.equals("admin@gmail.com") && pass.equals("123456")) {
                Intent intent = new Intent(LoginActivity.this, AddItemActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        btnLogin = findViewById(R.id.btnLogin);
        editPass = findViewById(R.id.editPass);
        btnRegister = findViewById(R.id.btnRegister);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        editTextEmail = findViewById(R.id.editEmail);
        btnRegister.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                                               startActivity(intent);
                                           }
                                       }
        );
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                final String email = editTextEmail.getText().toString();
                final String password = editPass.getText().toString();
                boolean flag;
                if (email.equals("")) {
                    editTextEmail.setError("Email Required");
                    flag = false;
                } else {
                    flag = true;
                    editTextEmail.setError(null);
                }
                if (password.equals("")) {
                    editPass.setError("Password Required");
                    flag = false;
                } else {
                    flag = true;
                    editPass.setError(null);
                }
                if (flag) {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserPojo userPojo = snapshot.getValue(UserPojo.class);
                                if (email.equals(userPojo.getEmail()) && password.equals(userPojo.getPassword())) {
                                    editor.putString("Email", email);
                                    editor.putString("Password", password);
                                    editor.putString("name", userPojo.getName());
                                    editor.putString("id", userPojo.getKey());
                                    editor.putString("imageUrl", userPojo.getImageurl());
                                    editor.putBoolean("LoginStatus", true);
                                    editor.commit();
                                    Toast.makeText(getApplicationContext(), "Login Successsfull", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                if (email.equals("admin@gmail.com") && password.equals("123456")) {
                                    editor.putString("Email", email);
                                    editor.putString("Password", password);
                                    editor.putBoolean("LoginStatus", true);
                                    editor.commit();
                                    Toast.makeText(getApplicationContext(), "Login Successsfull", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, AddItemActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }

    public void setActionBar(String Login_Activity) {
    }

}


