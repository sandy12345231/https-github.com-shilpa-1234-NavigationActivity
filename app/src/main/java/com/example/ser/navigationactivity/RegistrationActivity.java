package com.example.ser.navigationactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegistrationActivity extends AppCompatActivity {
    EditText editname, editnumber, editpassword, editrepassword, editemail;
    DatabaseReference databaseReference;
    Button register;
    ImageView imageview;
    SelectImageHelper selectImageHelper;
    Uri uri;
    private StorageReference mStorageRef;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        register= findViewById(R.id.register);
        editname =  findViewById(R.id.editname);
        editnumber =  findViewById(R.id.editnumber);
        editpassword =  findViewById(R.id.editpassword);
        editemail =  findViewById(R.id.editemail);
        imageview =  findViewById(R.id.imageview);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progress = new ProgressDialog(this);
        progress.setCancelable(true);
        progress.setTitle("Please wait...");
        selectImageHelper = new SelectImageHelper(RegistrationActivity.this, imageview);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageHelper.selectImageOption();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        editrepassword =  findViewById(R.id.editrepassword);
        register =  findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                final String name = editname.getText().toString();
                final String number = editnumber.getText().toString();
                final String password = editpassword.getText().toString();
                final String email = editemail.getText().toString();
                final String confirmpass = editrepassword.getText().toString();
                Uri uri = selectImageHelper.getURI_FOR_SELECTED_IMAGE();
                boolean flag, isProfile = false;
                if (name.equals("")) {
                    editname.setError("Name Required");
                    flag = false;
                    progress.cancel();
                } else {
                    flag = true;
                    editname.setError(null);
                }
                if (number.equals("")) {
                    editnumber.setError("Number Required");
                    flag = false;
                    progress.cancel();
                } else {
                    flag = true;
                    editnumber.setError(null);
                }
                if (password.equals("") && password.length() > 6 ) {
                    editpassword.setError("Password Required");
                    flag = false;
                    progress.cancel();
                } else {
                    flag = true;
                    editpassword.setError(null);
                }
                if (email.equals("")) {
                    editemail.setError("Email Required");
                    flag = false;
                    progress.cancel();
                } else {
                    flag = true;
                    editemail.setError(null);
                }

                if (!password.equals(confirmpass)) {
                    editrepassword.setError("Password doesn't match");
                    flag = false;
                    progress.cancel();
                } else {
                    flag = true;
                    editrepassword.setError(null);
                }
                if (uri == null) {
                    flag = false;
                    Toast.makeText(RegistrationActivity.this, "Profile Photo Required!", Toast.LENGTH_SHORT).show();
                }else{
                    flag = true;
                }
                if (flag) {
                    final StorageReference riversRef = mStorageRef.child("/profile/" + name);
                    riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progress.cancel();
                                    final String id = databaseReference.push().getKey();
                                    UserPojo userPojo1 = new UserPojo();
                                    userPojo1.setEmail(email);
                                    userPojo1.setName(name);
                                    userPojo1.setPassword(password);
                                    userPojo1.setNumber(number);
                                    userPojo1.setNumber(number);
                                    userPojo1.setKey(id);
                                    userPojo1.setImageurl(uri.toString());
                                    Toast.makeText(RegistrationActivity.this, "User Register Successfully", Toast.LENGTH_SHORT).show();
                                    databaseReference.child(id).setValue(userPojo1);
                                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progress.cancel();
                                    Toast.makeText(RegistrationActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progress.cancel();
                            Toast.makeText(RegistrationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        selectImageHelper.handleResult(requestCode, resultCode, result);  // call this helper class method
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        selectImageHelper.handleGrantedPermisson(requestCode, grantResults);   // call this helper class method
    }
}
