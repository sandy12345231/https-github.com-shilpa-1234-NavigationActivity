package com.example.ser.navigationactivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;



public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private Context context;
    ArrayList<OrderPojo> arraylist;


    public OrderAdapter(Context context, ArrayList<OrderPojo> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
    }

    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.order_item, parent, false);
        OrderAdapter.MyViewHolder viewHolder = new OrderAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderAdapter.MyViewHolder holder, final int position) {
        holder.textTotal.setText("Total Amount : ₹ "+arraylist.get(position).getTotalPrice());
        holder.textPaybleamount.setText("Payable Amount :  ₹ " + arraylist.get(position).getPayableAmount());
        holder.textQuantity.setText("Quantity : " + arraylist.get(position).getQuantity());
        holder.textId.setText("Order Id : "+arraylist.get(position).getOrderId());
        holder.textStatus.setText(arraylist.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textTotal, textQuantity, textPaybleamount,textId, textStatus;


        public MyViewHolder(View viewlist) {
            super(viewlist);
            textTotal = (TextView) viewlist.findViewById(R.id.textTotal);
            textQuantity = (TextView) viewlist.findViewById(R.id.textQuantity);
            textPaybleamount = (TextView) viewlist.findViewById(R.id.textPaybleamount);
            textId = (TextView) viewlist.findViewById(R.id.textId);
            textStatus = (TextView) viewlist.findViewById(R.id.textStatus);
        }


    }
}





