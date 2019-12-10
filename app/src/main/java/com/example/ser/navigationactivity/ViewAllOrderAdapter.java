package com.example.ser.navigationactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


class ViewAllOrderAdapter extends RecyclerView.Adapter<ViewAllOrderAdapter.MyViewHolder> {

    private Context context;
    ArrayList<OrderPojo> arraylist;
   private String status;


     ViewAllOrderAdapter(Context context, ArrayList<OrderPojo> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
    }

    @Override
   public ViewAllOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.order_item, parent, false);
        ViewAllOrderAdapter.MyViewHolder viewHolder = new ViewAllOrderAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewAllOrderAdapter.MyViewHolder holder, final int position) {
        holder.textTotal.setText("Total Amount : ₹ " + arraylist.get(position).getTotalPrice());
        holder.textPaybleamount.setText("Payable Amount :  ₹ " + arraylist.get(position).getPayableAmount());
        holder.textQuantity.setText("Quantity : " + arraylist.get(position).getQuantity());
        holder.textId.setText("Order Id : " + arraylist.get(position).getOrderId());
        holder.textStatus.setText(arraylist.get(position).getStatus());
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String arr[] = {"Pending","Completed"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Check Status");
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = layoutInflater.inflate(R.layout.change_status, null);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                final Spinner spinner =  dialogView.findViewById(R.id.spinner);
                TextView textTotal =  dialogView.findViewById(R.id.textTotal);
                TextView textQuantity =  dialogView.findViewById(R.id.textQuantity);
                TextView textPaybleamount =  dialogView.findViewById(R.id.textPaybleamount);
                textTotal.setText("Total Amount : ₹ " + arraylist.get(position).getTotalPrice());
                textPaybleamount.setText("Payable Amount :  ₹ " + arraylist.get(position).getPayableAmount());
                textQuantity.setText("Quantity : " + arraylist.get(position).getQuantity());

                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, arr);
                spinner.setAdapter(adapter);
               final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OrderInfo");
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        status = spinner.getAdapter().getItem(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Log.d("1234", "onClick: "+status);
                Button buttonOk = (Button) dialogView.findViewById(R.id.buttonOk);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("1234", "onClick: adapter");
                        HashMap<String,Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("status",status);
                        reference.child(arraylist.get(position).getOrderId()).updateChildren(hashMap);
                        alertDialog.dismiss();
                        Toast.makeText(context, "Product Delivered!", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                });

                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textTotal, textQuantity, textPaybleamount, textId, textStatus;
        RelativeLayout frameLayout;

        public MyViewHolder(View viewlist) {
            super(viewlist);
            textTotal =  viewlist.findViewById(R.id.textTotal);
            textQuantity = viewlist.findViewById(R.id.textQuantity);
            textPaybleamount =  viewlist.findViewById(R.id.textPaybleamount);
            textId =  viewlist.findViewById(R.id.textId);
            textStatus =  viewlist.findViewById(R.id.textStatus);
            frameLayout =  viewlist.findViewById(R.id.frameLayout);
        }


    }
}
