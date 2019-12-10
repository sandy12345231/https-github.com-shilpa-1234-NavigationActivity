package com.example.ser.navigationactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class AddItemActivity extends AppCompatActivity {
    ArrayAdapter<String> arrayAdapter;
    Spinner spinner;
    EditText textBrandName, textprice;
    ImageView imageView3;
    Button buttonsave;
    String Category[] = {"T-shirt", "Keychain", "Handbag", "Shoes", "Jewelery", "Visiting Card"};
    Uri uri = null;
    String categoryItem;
    private StorageReference mStorageRef;
    LinearLayout linearLayout;
    DatabaseReference databaseRef;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        spinner = (Spinner) findViewById(R.id.spinner);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        buttonsave = (Button) findViewById(R.id.buttonsave);
        textprice = (EditText) findViewById(R.id.textprice);
        textBrandName = (EditText) findViewById(R.id.textFoodName);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        progress = new ProgressDialog(this);
        progress.setCancelable(true);
        progress.setTitle("Please wait...");
        databaseRef = FirebaseDatabase.getInstance().getReference("product");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Category);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryItem = Category[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                final String brandName = textBrandName.getText().toString();
                final String itemPrice = textprice.getText().toString();

                boolean flag;
                if (brandName.equals("")) {
                    flag = false;
                    progress.cancel();
                    Toast.makeText(AddItemActivity.this, "Name Required", Toast.LENGTH_SHORT).show();
                } else {
                    flag = true;
                    textBrandName.setError(null);
                }
                if (itemPrice.equals("")) {
                    flag = false;
                    progress.cancel();
                    Toast.makeText(AddItemActivity.this, "Price Required", Toast.LENGTH_SHORT).show();
                } else {
                    flag = true;
                    textprice.setError(null);
                }
                if (uri == null) {
                    flag = false;
                    progress.cancel();
                    Toast.makeText(AddItemActivity.this, "Item Photo Required", Toast.LENGTH_SHORT).show();
                } else {
                    flag = true;
                }
                if (flag) {
                    final StorageReference riversRef = mStorageRef.child("/image/" + brandName + itemPrice);
                    riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ProductPojo productPojo = new ProductPojo();
                                    productPojo.setBrandname(brandName);
                                    productPojo.setCategory(categoryItem);
                                    productPojo.setImageUrl(uri.toString());
                                    productPojo.setPrice(itemPrice);
                                    String key = databaseRef.push().getKey();
                                    productPojo.setKey(key);
                                    databaseRef.child(key).setValue(productPojo);
                                    progress.cancel();
                                    Toast.makeText(AddItemActivity.this, "Item Added!", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progress.cancel();
                                    Toast.makeText(AddItemActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progress.cancel();
                            Toast.makeText(AddItemActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

    }

    public void uploadImage(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 110);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 110 && resultCode == RESULT_OK) {
            linearLayout.setVisibility(View.GONE);
            imageView3.setImageURI(data.getData());
            uri = data.getData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent inte = new Intent(AddItemActivity.this, LoginActivity.class);
                startActivity(inte);
                finish();
                break;
            case R.id.viewOrder:
                Intent intent = new Intent(AddItemActivity.this, ViewAllOrderActivity.class);
                startActivity(intent);
                   finish();
                break;

        }


        return true;
    }
}