package com.example.pasardirekapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasardirekapp.Classes.Customer;
import com.example.pasardirekapp.Classes.Supplier;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ContactDetailActivity extends AppCompatActivity {

    final int VIEW_TYPE_SUPPLIER = 0;
    final int VIEW_TYPE_CONSUMER = 1;

    Intent intent;
    FirebaseDatabaseHelper dbHelper;
    TextView tvName, tvPhone, tvCompanyName, tvCompanyName2, tvCustomerGrade, tvCustomerGrade2;
    EditText etName, etPhone, etCompanyName;
    Button btnEditContact;
    DatabaseReference suppliersReference, customersReference;
    ValueEventListener suppliersValueEventListener, customersValueEventListener;
    Customer customer;
    Supplier supplier;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        suppliersReference.removeEventListener(suppliersValueEventListener);
        customersReference.removeEventListener(customersValueEventListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        dbHelper = new FirebaseDatabaseHelper();

        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvCompanyName2 = findViewById(R.id.tvCompanyName2);
        tvCustomerGrade = findViewById(R.id.tvCustomerGrade);
        tvCustomerGrade2 = findViewById(R.id.tvCustomerGrade2);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etCompanyName = findViewById(R.id.etCompanyName);
        btnEditContact = findViewById(R.id.btnEdit);

        intent = getIntent();
        String id = intent.getStringExtra("id");
        final int type = intent.getIntExtra("type", -1);

        customersReference = dbHelper.retrieveReference("Contacts").child("Customers").child(id);
        customersValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customer = dataSnapshot.getValue(Customer.class);
                String levelArray[] = getResources().getStringArray(R.array.customer_level);
                tvName.setText(customer.name);
                tvPhone.setText(customer.phone);
                tvCompanyName.setText("# of Transactions");
                tvCompanyName2.setText("" + customer.getTrans_no());
                tvCustomerGrade2.setText("" + levelArray[customer.getGrade()]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        suppliersReference = dbHelper.retrieveReference("Contacts").child("Suppliers").child(id);
        suppliersValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                supplier = dataSnapshot.getValue(Supplier.class);
                tvName.setText(supplier.name);
                tvPhone.setText(supplier.phone);
                tvCompanyName2.setText(supplier.getCompanyName());
                tvCustomerGrade.setVisibility(TextView.GONE);
                tvCustomerGrade2.setVisibility(TextView.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        btnEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == VIEW_TYPE_CONSUMER) {
                    customer.applyChanges(etName.getText().toString(), etPhone.getText().toString());
                    customersReference.setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ContactDetailActivity.this, "Successfully edited customer.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if (type == VIEW_TYPE_SUPPLIER) {
                    supplier.applyChanges(etName.getText().toString(), etPhone.getText().toString(), etCompanyName.getText().toString());
                    suppliersReference.setValue(supplier).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ContactDetailActivity.this, "Successfully edited supplier.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        if (type == VIEW_TYPE_CONSUMER) {
            customersReference.addValueEventListener(customersValueEventListener);
            etCompanyName.setVisibility(View.GONE);
        } else if (type == VIEW_TYPE_SUPPLIER) {
            suppliersReference.addValueEventListener(suppliersValueEventListener);
        } else {
            Toast.makeText(ContactDetailActivity.this, "Inappropriate Access", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
