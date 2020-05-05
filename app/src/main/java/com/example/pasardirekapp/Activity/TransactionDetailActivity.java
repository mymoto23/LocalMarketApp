package com.example.pasardirekapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasardirekapp.Classes.BuyTrans;
import com.example.pasardirekapp.Classes.SellTrans;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class TransactionDetailActivity extends AppCompatActivity {

    final int TRANSACTION_TYPE_BUY = 0;
    final int TRANSACTION_TYPE_SELL = 1;

    Intent intent;
    FirebaseDatabaseHelper dbHelper;
    TextView tvProductName, textView, tvQuantity, tvAmount, tvDate, tvContactName;
    ValueEventListener buyValueEventListener, sellValueEventListener;

    DatabaseReference buyRef, sellRef;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        buyRef.removeEventListener(buyValueEventListener);
        sellRef.removeEventListener(sellValueEventListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        intent = getIntent();

        tvContactName = findViewById(R.id.tvContactName);
        tvProductName = findViewById(R.id.tvProductName);
        textView = findViewById(R.id.textView3);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvAmount = findViewById(R.id.tvAmount);
        tvDate = findViewById(R.id.tvDate);

        dbHelper = new FirebaseDatabaseHelper();

        String id = intent.getStringExtra("id");
        int transaction_type = intent.getIntExtra("type", -1);

        buyValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BuyTrans buyTrans = dataSnapshot.getValue(BuyTrans.class);
                tvProductName.setText(buyTrans.product.getName());
                tvContactName.setText(buyTrans.getSupplier().name);
                textView.setText("Supplier");
                tvQuantity.setText("" + buyTrans.quantity);
                tvAmount.setText("" + buyTrans.amount);
                tvDate.setText(dbHelper.convertDateToString(buyTrans.date));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        sellValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SellTrans buyTrans = dataSnapshot.getValue(SellTrans.class);
                tvProductName.setText(buyTrans.product.getName());
                tvContactName.setText(buyTrans.getCustomer().name);
                textView.setText("Customer");
                tvQuantity.setText("" + buyTrans.quantity);
                tvAmount.setText("" + buyTrans.amount);
                tvDate.setText(dbHelper.convertDateToString(buyTrans.date));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        buyRef = dbHelper.retrieveReference("Transactions").child("Buy").child(id);
        sellRef = dbHelper.retrieveReference("Transactions").child("Sell").child(id);

        if (transaction_type == TRANSACTION_TYPE_BUY) {
            buyRef.addValueEventListener(buyValueEventListener);
        }
        else if (transaction_type == TRANSACTION_TYPE_SELL) {
            sellRef.addValueEventListener(sellValueEventListener);
        }
        else {
            Toast.makeText(TransactionDetailActivity.this, "Inappropriate Access", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
