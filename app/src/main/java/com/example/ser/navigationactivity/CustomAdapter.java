package com.example.ser.navigationactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductPojo> arrayList;
     SharedPreferences sharedPreferences;


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cart");

    public CustomAdapter(Context context, ArrayList<ProductPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.product, parent, false);
        CustomAdapter.ViewHolder vh = new CustomAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d("1234", "onBindViewHolder: "+arrayList.size());
        sharedPreferences = context.getSharedPreferences("Data",Context.MODE_PRIVATE);
        holder.editText.setText(arrayList.get(position).getBrandname());
        holder.price.setText("₹" + arrayList.get(position).getPrice());
        Log.d("1234", "onBindViewHolder: " + sharedPreferences.getString("id",null));
       // Glide.with(context).load(arrayList.get(position).getImageUrl()).into(holder.imageView);
        holder.addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = layoutInflater.inflate(R.layout.alertdialogdesign, null);
                builder.setView(dialogView);
                builder.setTitle("Add item");
                final AlertDialog alertDialog = builder.create();
                ImageView imageInc = (ImageView) dialogView.findViewById(R.id.additem);
                ImageView imageDec = (ImageView) dialogView.findViewById(R.id.removeitem);
                Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAdd);
                final TextView quantity = (TextView) dialogView.findViewById(R.id.editQuantity);
                TextView textViewBrandName = (TextView) dialogView.findViewById(R.id.textbrand);
                TextView textViewPrice = (TextView) dialogView.findViewById(R.id.Editprice);
                ImageView imageViewItemProfile = (ImageView) dialogView.findViewById(R.id.image);
                textViewBrandName.setText(arrayList.get(position).getBrandname());
                textViewPrice.setText(arrayList.get(position).getPrice()+" Rs");
                //Glide.with(context).load(arrayList.get(position).getImageUrl()).into(imageViewItemProfile);


                imageInc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantity.setText(String.valueOf(Integer.parseInt(quantity.getText().toString()) + 1));
                    }
                });
                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String userid = databaseReference.push().getKey();
                        Toast.makeText(context, "Item Added!", Toast.LENGTH_LONG).show();
                        Log.d("12345", "onClick: "+arrayList.get(position).getImageUrl());
                        CartPojo cartPojo = new CartPojo();
                        cartPojo.setImageUrl(arrayList.get(position).getImageUrl());
                        cartPojo.setBrandname(arrayList.get(position).getBrandname());
                        cartPojo.setPrice(arrayList.get(position).getPrice());
                        cartPojo.setQuantity(quantity.getText().toString());
                        cartPojo.setUserId(sharedPreferences.getString("id",null));
                        cartPojo.setId(userid);
                        databaseReference.child(userid).setValue(cartPojo);
                       alertDialog.dismiss();

                    }
                });
                imageDec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (quantity.getText().toString().equals("1")) {
                            quantity.setText("1");
                        } else {
                            if (!quantity.getText().toString().equals("1"))
                                quantity.setText(String.valueOf(Integer.parseInt(quantity.getText().toString()) - 1));
                        }
                    }


                });

                alertDialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView editText, price;
        public ImageView imageView;
        public Button addcard;

        public ViewHolder(View viewitem) {
            super(viewitem);
            editText = (TextView) viewitem.findViewById(R.id.brandname);
            price = (TextView) viewitem.findViewById(R.id.price);
            imageView = (ImageView) viewitem.findViewById(R.id.imageview1);
            addcard = (Button)viewitem.findViewById(R.id.addCart);


        }


    }
}
