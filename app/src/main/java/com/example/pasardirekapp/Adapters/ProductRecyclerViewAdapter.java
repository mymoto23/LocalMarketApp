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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pasardirekapp.Activity.AddCustomerActivity;
import com.example.pasardirekapp.Activity.AddTransactionActivity;
import com.example.pasardirekapp.Activity.ProductDetailActivity;
import com.example.pasardirekapp.Classes.Product;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;

import java.util.ArrayList;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private ArrayList<CardView> cardViewList = new ArrayList<>();
    private ArrayList<Product> products;
    private ArrayList<String> product_id;
    private LayoutInflater inflater;
    private Context context;
    private Product selectedproduct;
    private String selectedProductId;

    public ProductRecyclerViewAdapter(Context context, ArrayList<Product> products, ArrayList<String> id) {
        this.product_id = id;
        this.products = products;
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
        holder.itemName.setText(products.get(position).getName());
        holder.description.setText(products.get(position).getDescription());
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
                    selectedproduct = products.get(pos);
                    selectedProductId = product_id.get(pos);
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
                                    dbHelper.retrieveReference("Products").child(product_id.get(position)).removeValue();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("key", product_id.get(pos));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public Product getSelectedProduct() {
        return selectedproduct;
    }

    public String getSelectedProductId() {
        return selectedProductId;
    }

}
