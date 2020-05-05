package com.example.pasardirekapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pasardirekapp.Classes.Supplier;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.android.gms.tasks.OnSuccessListener;

public class AddSupplierActivity extends AppCompatActivity {

    FirebaseDatabaseHelper dbHelper;
    EditText etName, etPhone, etCompanyName;
    Button btnAddSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);

        dbHelper = new FirebaseDatabaseHelper();
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etCompanyName = findViewById(R.id.etCompanyName);

        btnAddSupplier = findViewById(R.id.btnaddSupplier);

        btnAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Supplier supplier = new Supplier(etName.getText().toString(), etPhone.getText().toString(), etCompanyName.getText().toString());
                    dbHelper.retrieveReference("Contacts")
                            .child("Suppliers")
                            .push()
                            .setValue(supplier)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddSupplierActivity.this, "Successfully added supplier", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }
            }
        });

    }
    public boolean validate() {
        return !etName.getText().toString().isEmpty() &&
                !etCompanyName.getText().toString().isEmpty() &&
                !etPhone.getText().toString().isEmpty();
    }
}
