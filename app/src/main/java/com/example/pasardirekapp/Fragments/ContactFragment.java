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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pasardirekapp.Activity.AddCustomerActivity;
import com.example.pasardirekapp.Activity.AddSupplierActivity;
import com.example.pasardirekapp.Adapters.ContactRecyclerViewAdapter;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class ContactFragment extends Fragment {

    RecyclerView rv;
    Button btnAddContact;
    RadioGroup filterRadioGroup;
    RadioButton radioButtonCustomer, radioButtonSupplier;
    TextView tvResetFilter;
    Context context;
    ContactRecyclerViewAdapter adapter;
    FirebaseDatabaseHelper dbHelper;
    View view;
    ArrayList<DataSnapshot> customers, customers_final;
    ArrayList<DataSnapshot> suppliers, suppliers_final;
    DatabaseReference SupplierReference, CustomerReference;
    ChildEventListener supplierChildEventListener, customerChildEventListener;

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new FirebaseDatabaseHelper();
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        rv = view.findViewById(R.id.rvContacts);
        btnAddContact = view.findViewById(R.id.btnAddContact);
        filterRadioGroup = view.findViewById(R.id.filterRadioGroup);
        radioButtonCustomer = view.findViewById(R.id.radioButtonCustomer);
        radioButtonSupplier = view.findViewById(R.id.radioButtonSupplier);
        tvResetFilter = view.findViewById(R.id.tvResetFilter);
        context = view.getContext();
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        suppliers = new ArrayList<>();
        customers = new ArrayList<>();
        suppliers_final = new ArrayList<>();
        customers_final = new ArrayList<>();

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Add Contact")
                        .setMessage("Are you adding a customer or a supplier?")
                        .setPositiveButton("Customer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               startActivity(new Intent(context, AddCustomerActivity.class));
                            }
                        })
                        .setNegativeButton("Supplier", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(context, AddSupplierActivity.class));
                            }
                        }).show();
            }
        });

        tvResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRadioGroup.clearCheck();
                filterContacts();
            }
        });

        filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                filterContacts();
            }
        });

        SupplierReference = dbHelper.retrieveReference("Contacts").child("Suppliers");
        supplierChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                suppliers.add(dataSnapshot);
                suppliers_final.add(dataSnapshot);
                adapter = new ContactRecyclerViewAdapter(context, suppliers, customers);
                rv.setAdapter(adapter);

                if (customers.isEmpty() && suppliers.isEmpty()) {
                    Toast.makeText(context, "No contacts" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int position = -1;
                for (int i = 0; i < suppliers.size(); i++) {
                    if (suppliers.get(i).getKey().equals(dataSnapshot.getKey())) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    suppliers.set(position, dataSnapshot);
                    suppliers_final.set(position, dataSnapshot);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int position = -1;
                for (int i = 0; i < suppliers.size(); i++) {
                    if (suppliers.get(i).getKey().equals(dataSnapshot.getKey())) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    suppliers.remove(position);
                    suppliers_final.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        SupplierReference.addChildEventListener(supplierChildEventListener);

        CustomerReference = dbHelper.retrieveReference("Contacts").child("Customers");
        customerChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                customers.add(dataSnapshot);
                customers_final.add(dataSnapshot);
                adapter = new ContactRecyclerViewAdapter(context, suppliers, customers);
                rv.setAdapter(adapter);

                if (customers.isEmpty() && suppliers.isEmpty()) {
                    Toast.makeText(context, "No contacts" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                int position = dataSnapshotArrayList.indexOf(dataSnapshot);
                int position = -1;
                for (int i = 0; i < customers.size(); i++) {
                    if (customers.get(i).getKey().equals(dataSnapshot.getKey())) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    customers.set(position, dataSnapshot);
                    customers_final.set(position, dataSnapshot);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                int position = dataSnapshotArrayList.indexOf(dataSnapshot);
                int position = -1;
                for (int i = 0; i < customers.size(); i++) {
                    if (customers.get(i).getKey().equals(dataSnapshot.getKey())) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    customers.remove(position);
                    customers_final.remove(position);
                    adapter.notifyDataSetChanged();
                }

                if (customers.isEmpty() && suppliers.isEmpty()) {
                    Toast.makeText(context, "No contacts" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        CustomerReference.addChildEventListener(customerChildEventListener);
//      TODO: Implement filter with radio button
        return view;
    }

//    public void onCustomerRadioClicked(View v) {
//        if (((RadioButton)v).isChecked())
//            radioButtonCustomer.setChecked(false);
//        else
//            radioButtonCustomer.setChecked(true);
//        filterContacts();
//    }
//
//    public void onSupplierRadioClicked(View v) {
//        if (((RadioButton)v).isChecked())
//            radioButtonSupplier.setChecked(false);
//        else
//            radioButtonSupplier.setChecked(true);
//        filterContacts();
//    }

    public void filterContacts() {
        if (radioButtonSupplier.isChecked() && radioButtonCustomer.isChecked()) {
            customers.clear();
            customers.addAll(customers_final);
            suppliers.clear();
            suppliers.addAll(suppliers_final);
        } else if (radioButtonCustomer.isChecked()) {
            suppliers.clear();
            customers.clear();
            customers.addAll(customers_final);
        } else if (radioButtonSupplier.isChecked()) {
            customers.clear();
            suppliers.clear();
            suppliers.addAll(suppliers_final);
        } else {
            customers.clear();
            customers.addAll(customers_final);
            suppliers.clear();
            suppliers.addAll(suppliers_final);
        }

        adapter.notifyDataSetChanged();
    }
}
