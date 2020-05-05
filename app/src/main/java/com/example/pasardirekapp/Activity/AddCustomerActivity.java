package com.example.pasardirekapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pasardirekapp.Classes.Customer;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.android.gms.tasks.OnSuccessListener;

public class AddCustomerActivity extends AppCompatActivity {

    EditText etName, etPhone;
    Button btnAddCustomer;
    FirebaseDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        dbHelper = new FirebaseDatabaseHelper();

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btnAddCustomer = findViewById(R.id.btnAddCustomer);

        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Customer customer = new Customer(etName.getText().toString(), etPhone.getText().toString(), 0, 0);
                    dbHelper.retrieveReference("Contacts")
                            .child("Customers").push()
                            .setValue(customer)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddCustomerActivity.this, "Customer successfully added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });

    }

    public boolean validate() {
        return !etName.getText().toString().isEmpty() && !etPhone.getText().toString().isEmpty();
    }
}
