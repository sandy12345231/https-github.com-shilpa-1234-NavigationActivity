package com.example.ser.navigationactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.example.ser.navigationactivity.R.id.price;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private Context context;
    ArrayList<CartPojo> arraylist;
    CartPojo cartPojo;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cart");



    public CartAdapter(Context context, ArrayList<CartPojo> arraylist) {
        Log.d("1234", "CartAdapter: " + arraylist.size());
        this.context = context;
        this.arraylist = arraylist;
    }

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("1234", "onCreateViewHolder: ");
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cartdesign, parent, false);
        CartAdapter.MyViewHolder viewHolder = new CartAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CartAdapter.MyViewHolder holder, final int position) {
        holder.CartbrandName.setText(arraylist.get(position).getBrandname());
        holder.CartPrice.setText("Amount:  ₹" + arraylist.get(position).getPrice());
        holder.CartQuantity.setText("Quantity: " + arraylist.get(position).getQuantity());
       // Glide.with(context).load(arraylist.get(position).getImageUrl()).into(holder.Cartimage);

        holder.CartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("1234", "onDataChange: " + arraylist.size());
                        databaseReference.child(arraylist.get(position).getId()).removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView CartPrice, CartbrandName, CartQuantity;
        public ImageView CartDelete, Cartimage;


        public MyViewHolder(View viewlist) {
            super(viewlist);
            CartbrandName = (TextView) viewlist.findViewById(R.id.CartbrandName);
            Cartimage = (ImageView) viewlist.findViewById(R.id.Cartimage);
            CartQuantity = (TextView) viewlist.findViewById(R.id.CartQuantity);
            CartPrice = (TextView) viewlist.findViewById(R.id.CartPrice);
            CartDelete = (ImageView) viewlist.findViewById(R.id.CartDelete);


        }


    }
}




