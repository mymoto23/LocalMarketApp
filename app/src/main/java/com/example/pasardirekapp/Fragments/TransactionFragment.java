package com.example.pasardirekapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pasardirekapp.Activity.AddTransactionActivity;
import com.example.pasardirekapp.Adapters.TransactionRecyclerViewAdapter;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;


public class TransactionFragment extends Fragment {

    View view;
    RecyclerView rv;
    Button btnAddTransaction;
    Context context;
    TransactionRecyclerViewAdapter adapter;
    FirebaseDatabaseHelper dbHelper;
    ArrayList<DataSnapshot> buyTrans;
    ArrayList<DataSnapshot> sellTrans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transaction, container, false);
        context = view.getContext();

        rv = view.findViewById(R.id.rvTransactions);
        btnAddTransaction = view.findViewById(R.id.btnAddTrans);

        dbHelper = new FirebaseDatabaseHelper();

        buyTrans = new ArrayList<>();
        sellTrans = new ArrayList<>();

        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        dbHelper.retrieveReference("Transactions").child("Buy").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                buyTrans.add(dataSnapshot);
                adapter = new TransactionRecyclerViewAdapter(context, buyTrans, sellTrans);
                rv.setAdapter(adapter);

                if (buyTrans.isEmpty() && sellTrans.isEmpty()) {
                    Toast.makeText(context, "No contacts" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int position = -1;
                for (int i = 0; i < buyTrans.size(); i++) {
                    if (buyTrans.get(i).getKey().equals(dataSnapshot.getKey())) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    buyTrans.set(position, dataSnapshot);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int position = -1;
                for (int i = 0; i < buyTrans.size(); i++) {
                    if (buyTrans.get(i).getKey().equals(dataSnapshot.getKey())) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    buyTrans.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbHelper.retrieveReference("Transactions").child("Sell").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                sellTrans.add(dataSnapshot);
                adapter = new TransactionRecyclerViewAdapter(context, buyTrans, sellTrans);
                rv.setAdapter(adapter);

                if (buyTrans.isEmpty() && sellTrans.isEmpty()) {
                    Toast.makeText(context, "No contacts" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int position = -1;
                for (int i = 0; i < sellTrans.size(); i++) {
                    if (sellTrans.get(i).getKey().equals(dataSnapshot.getKey())) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    sellTrans.set(position, dataSnapshot);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int position = -1;
                for (int i = 0; i < sellTrans.size(); i++) {
                    if (sellTrans.get(i).getKey().equals(dataSnapshot.getKey())) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    sellTrans.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Add Transaction")
                        .setMessage("Are you adding a purchase or sell?")
                        .setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(context, AddTransactionActivity.class);
                                intent.putExtra("type", 0);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Sell", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(context, AddTransactionActivity.class);
                                intent.putExtra("type", 1);
                                startActivity(intent);
                            }
                        }).show();
//                context.startActivity(new Intent(context, AddTransactionActivity.class));
            }
        });
        return view;
    }

}
