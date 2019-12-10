package com.example.ser.navigationactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    SharedPreferences sharedPreferences;
    Context context;
    ArrayList<AddressPojo> arrayList;
    DatabaseReference databaseReference;
    String userId;

    public AddressAdapter(Context context, ArrayList<AddressPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.address_show, parent, false);
        AddressAdapter.ViewHolder vh = new AddressAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Address");
        sharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("id", null);
        holder.textCity.setText("City : " + arrayList.get(position).getCity());
        holder.textArea.setText("Area : " + arrayList.get(position).getArea());
        holder.textState.setText("State : " + arrayList.get(position).getState());
        holder.textName.setText("Name : " + arrayList.get(position).getName());
        holder.textPincode.setText("Pin : " + arrayList.get(position).getPincode());
        if (arrayList.get(position).getStatus().equals("true"))
        {
            holder.relativeLayout.setVisibility(View.VISIBLE);
            if (context instanceof AddressSetActivity) {
               // ((AddressSetActivity)context).callProceedToPayment();
            }
        }
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("status","true");
                databaseReference.child(arrayList.get(position).getAddId()).updateChildren(hashMap);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            AddressPojo pojo = snapshot.getValue(AddressPojo.class);
                            if (pojo.getAddId().equals(arrayList.get(position).getAddId()) && pojo.getUserId().equals(userId)){
                                notifyDataSetChanged();
                            }else{
                                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                                hashMap.put("status","false");
                                databaseReference.child(pojo.getAddId()).updateChildren(hashMap);
                                notifyDataSetChanged();
                            }
                        }

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
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textCity, textArea, textState, textPincode, textDistrict, textName;
        public FrameLayout frameLayout;
        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            textArea = (TextView) view.findViewById(R.id.textArea);
            textCity = (TextView) view.findViewById(R.id.textCity);
            textName = (TextView) view.findViewById(R.id.textName);
            textState = (TextView) view.findViewById(R.id.textState);
            textPincode = (TextView) view.findViewById(R.id.textPincode);
            frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        }


    }

}
