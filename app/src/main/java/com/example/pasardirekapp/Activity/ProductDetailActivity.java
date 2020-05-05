package com.example.pasardirekapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasardirekapp.Classes.Product;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ProductDetailActivity extends AppCompatActivity {

    FirebaseDatabaseHelper dbHelper;
    TextView tvName, tvDescription, tvPrice, tvQuantity, tvDate, tvEditedDate, tvSellPrice;
    EditText etName, etDescription, etPrice, etQuantity, etSellPrice;
    Button btnEdit;
    Product p;
    long editedDate;

    DatabaseReference productReference;
    ValueEventListener eventListener;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productReference.removeEventListener(eventListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        dbHelper = new FirebaseDatabaseHelper();

        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvDate = findViewById(R.id.tvDate);
        tvEditedDate = findViewById(R.id.tvSelectDate);
        tvSellPrice = findViewById(R.id.tvSellPrice);

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etSellPrice = findViewById(R.id.etSellPrice);
        etQuantity = findViewById(R.id.etQuantity);

        btnEdit = findViewById(R.id.btnEdit);

        Intent intent = getIntent();
        String product_id = intent.getStringExtra("key");

        productReference = dbHelper.retrieveReference("Products").child(product_id);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                p = dataSnapshot.getValue(Product.class);
                tvName.setText(p.getName());
                tvDescription.setText(p.getDescription());
                tvPrice.setText(Integer.toString(p.getBuy_price()));
                tvSellPrice.setText(Integer.toString(p.getSell_price()));
                tvQuantity.setText(Integer.toString(p.getQuantity()));
                tvDate.setText(dbHelper.convertDateToString(p.getDate()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        productReference.addValueEventListener(eventListener);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.applyChanges(etName.getText().toString(), etDescription.getText().toString(), etPrice.getText().toString(), etSellPrice.getText().toString(), etQuantity.getText().toString(), editedDate);
                productReference.setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ProductDetailActivity.this, "Product successfully edited.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tvEditedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ProductDetailActivity.this,
                        R.style.Theme_AppCompat_DayNight_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tvEditedDate.setText("" + (month + 1) + "/" + day + "/" + year);
                                Calendar newDate = new GregorianCalendar(year, month, day);
                                editedDate = newDate.getTimeInMillis();
                            }
                        },
                        year, month, day
                );
                dialog.show();
            }
        });


    }
}
