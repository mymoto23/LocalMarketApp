package com.example.pasardirekapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pasardirekapp.Activity.AddTransactionActivity;
import com.example.pasardirekapp.Activity.ContactDetailActivity;
import com.example.pasardirekapp.Activity.MainActivity;
import com.example.pasardirekapp.Classes.Customer;
import com.example.pasardirekapp.Classes.Supplier;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    final int VIEW_TYPE_SUPPLIER = 0;
    final int VIEW_TYPE_CONSUMER = 1;

    private ArrayList<CardView> cardViewList = new ArrayList<>();
    private ArrayList<DataSnapshot> suppliers;
    private ArrayList<DataSnapshot> consumers;
    private LayoutInflater inflater;
    private Context context;
    private DataSnapshot selectedContact;

    public ContactRecyclerViewAdapter(Context context, ArrayList<DataSnapshot> suppliers, ArrayList<DataSnapshot> consumers) {
        this.suppliers = suppliers;
        this.consumers = consumers;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_list_item, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_CONSUMER) {
            holder.itemName.setText(consumers.get(position).getValue(Customer.class).name);
            holder.description.setText(consumers.get(position).getValue(Customer.class).phone);
            if (context.getClass().equals(MainActivity.class)) {
                holder.cardView.setBackgroundColor(Color.parseColor("#ADD8E6"));
            }
            if (!cardViewList.contains(holder.cardView))
                cardViewList.add(holder.cardView);
            if (context.getClass().equals(AddTransactionActivity.class)){
                holder.btnDelete.setVisibility(View.GONE);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        for (CardView cardView : cardViewList) {
                            cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                        holder.cardView.setCardBackgroundColor(Color.parseColor("#CCFF00"));
                        selectedContact = consumers.get(position);
                    }
                });
            }
            else {
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(context)
                                .setTitle("Delete")
                                .setMessage("Are you sure?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();
                                        dbHelper.retrieveReference("Contacts").child("Customers").child(consumers.get(position).getKey()).removeValue();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                });

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Intent intent = new Intent(context, ContactDetailActivity.class);
                        Log.d("key", "" + consumers.get(position).getKey());
                        intent.putExtra("id", consumers.get(position).getKey());
                        intent.putExtra("type", VIEW_TYPE_CONSUMER);
                        context.startActivity(intent);
                    }
                });
            }
        } else {
            final int pos = position - consumers.size();
            holder.itemName.setText(suppliers.get(pos).getValue(Supplier.class).name);
            holder.description.setText(suppliers.get(pos).getValue(Supplier.class).phone);
            if (context.getClass().equals(MainActivity.class)) {
                holder.cardView.setBackgroundColor(Color.parseColor("#FFCCCB"));
            }
            if (!cardViewList.contains(holder.cardView))
                cardViewList.add(holder.cardView);
            if (context.getClass().equals(AddTransactionActivity.class)) {
                holder.btnDelete.setVisibility(View.GONE);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        for (CardView cardView : cardViewList) {
                            cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                        holder.cardView.setCardBackgroundColor(Color.parseColor("#CCFF00"));
                        selectedContact = suppliers.get(pos - consumers.size());
                    }
                });
            } else {
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(context)
                                .setTitle("Delete")
                                .setMessage("Are you sure?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();
                                        dbHelper.retrieveReference("Contacts").child("Suppliers").child(suppliers.get(pos).getKey()).removeValue();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                });

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Intent intent = new Intent(context, ContactDetailActivity.class);
                        intent.putExtra("id", suppliers.get(pos - consumers.size()).getKey());
                        intent.putExtra("type", VIEW_TYPE_SUPPLIER);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return consumers.size() + suppliers.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < consumers.size()) {
            return VIEW_TYPE_CONSUMER;
        }

        if (position - consumers.size() < suppliers.size()) {
            return VIEW_TYPE_SUPPLIER;
        }

        return -1;
    }

    public DataSnapshot getSelectedContact() {
        return selectedContact;
    }
}

