package com.example.pasardirekapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pasardirekapp.Activity.AddProductActivity;
import com.example.pasardirekapp.Adapters.ProductRecyclerViewAdapter;
import com.example.pasardirekapp.Classes.Product;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class ProductFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    RecyclerView rv;
    Spinner spinner;
    Button btnAddProduct;
    FirebaseDatabaseHelper dbHelper;
    ProductRecyclerViewAdapter adapter;
    ArrayList<Product> products, filteredProducts;
    ArrayList<String> products_id, filteredProducts_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dbHelper = new FirebaseDatabaseHelper();

        products = new ArrayList<>();
        products_id = new ArrayList<>();
        filteredProducts = new ArrayList<>();
        filteredProducts_id = new ArrayList<>();

        final View view = inflater.inflate(R.layout.fragment_product, container, false);
        rv = view.findViewById(R.id.rvProducts);
        spinner = view.findViewById(R.id.spnFilter);
        final Context context = view.getContext();
        btnAddProduct = view.findViewById(R.id.btnAddProduct);
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(context, R.array.categories_filter_array, R.layout.support_simple_spinner_dropdown_item);
        spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(spinner_adapter);
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(this);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddProductActivity.class));
            }
        });


        dbHelper.retrieveReference("Products").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                products.add(dataSnapshot.getValue(Product.class)); //add Product object
                products_id.add(dataSnapshot.getKey()); //add unique productID
                filteredProducts.add(dataSnapshot.getValue(Product.class)); //add Product object
                filteredProducts_id.add(dataSnapshot.getKey()); //add unique productID

                adapter = new ProductRecyclerViewAdapter(context, filteredProducts, filteredProducts_id);
                rv.setAdapter(adapter);

                if (products.isEmpty()) {
                    Toast.makeText(context, "No Products", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int position = products_id.indexOf(dataSnapshot.getKey());
                int filtered_position = filteredProducts_id.indexOf(dataSnapshot.getKey());
                products.set(position, dataSnapshot.getValue(Product.class));
                filteredProducts.set(filtered_position, dataSnapshot.getValue(Product.class));

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int position = products_id.indexOf(dataSnapshot.getKey());
                products.remove(position);
                products_id.remove(position);
                int filtered_position = filteredProducts_id.indexOf(dataSnapshot.getKey());
                filteredProducts.remove(filtered_position);
                filteredProducts_id.remove(filtered_position);
                adapter.notifyDataSetChanged();
                if (products.isEmpty()) {
                    Toast.makeText(context, "No Products", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        String selectedCategory = adapterView.getItemAtPosition(pos).toString();
        filteredProducts.clear();
        filteredProducts_id.clear();
        if (selectedCategory.equals("All")) {
            filteredProducts.addAll(products);
            filteredProducts_id.addAll(products_id);
        } else {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getCategory().equals(selectedCategory)) {
                    filteredProducts.add(products.get(i));
                    filteredProducts_id.add(products_id.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
