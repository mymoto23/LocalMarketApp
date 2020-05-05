package com.example.pasardirekapp.Adapters;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pasardirekapp.Activity.AddTransactionActivity;
import com.example.pasardirekapp.Activity.ContactDetailActivity;
import com.example.pasardirekapp.Activity.ProductDetailActivity;
import com.example.pasardirekapp.Activity.TransactionDetailActivity;
import com.example.pasardirekapp.Classes.BuyTrans;
import com.example.pasardirekapp.Classes.Customer;
import com.example.pasardirekapp.Classes.Product;
import com.example.pasardirekapp.Classes.SellTrans;
import com.example.pasardirekapp.Classes.Supplier;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private ArrayList<DataSnapshot> buyTrans;
    private ArrayList<DataSnapshot> sellTrans;
    private LayoutInflater inflater;
    private Context context;

    final int VIEW_TYPE_BUY = 0;
    final int VIEW_TYPE_SELL = 1;

    public TransactionRecyclerViewAdapter(Context context, ArrayList<DataSnapshot> buyTrans, ArrayList<DataSnapshot> sellTrans) {
        this.buyTrans = buyTrans;
        this.sellTrans = sellTrans;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
        if (getItemViewType(position) == VIEW_TYPE_SELL) {
            holder.itemName.setText(sellTrans.get(position).getValue(SellTrans.class).getCustomer().name + " - " + sellTrans.get(position).getValue(SellTrans.class).product.getName());
            holder.description.setText("+" + sellTrans.get(position).getValue(SellTrans.class).amount);
            holder.cardView.setBackgroundColor(Color.parseColor("#ADD8E6"));
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
                                    dbHelper.retrieveReference("Transactions").child("Sell").child(sellTrans.get(position).getKey()).removeValue();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    Intent intent = new Intent(context, TransactionDetailActivity.class);
                    intent.putExtra("id", sellTrans.get(position).getKey());
                    intent.putExtra("type", VIEW_TYPE_SELL);
                    context.startActivity(intent);
                }
            });
        } else {
            final int pos = position - sellTrans.size();
            holder.itemName.setText(buyTrans.get(pos).getValue(BuyTrans.class).getSupplier().name + " - " + buyTrans.get(pos).getValue(BuyTrans.class).product.getName());
            holder.description.setText("-" + buyTrans.get(pos).getValue(BuyTrans.class).amount);
            holder.cardView.setBackgroundColor(Color.parseColor("#FFCCCB"));
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
                                    dbHelper.retrieveReference("Transactions").child("Buy").child(buyTrans.get(pos).getKey()).removeValue();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    Intent intent = new Intent(context, TransactionDetailActivity.class);
                    intent.putExtra("id", buyTrans.get(pos - sellTrans.size()).getKey());
                    intent.putExtra("type", VIEW_TYPE_BUY);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return buyTrans.size() + sellTrans.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < sellTrans.size()) {
            return VIEW_TYPE_SELL;
        }

        if (position - sellTrans.size() < buyTrans.size()) {
            return VIEW_TYPE_BUY;
        }

        return -1;
    }
}
