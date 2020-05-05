package com.example.pasardirekapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasardirekapp.Adapters.ContactRecyclerViewAdapter;
import com.example.pasardirekapp.Adapters.ProductRecyclerViewAdapter;
import com.example.pasardirekapp.Classes.BuyTrans;
import com.example.pasardirekapp.Classes.Customer;
import com.example.pasardirekapp.Classes.Product;
import com.example.pasardirekapp.Classes.SellTrans;
import com.example.pasardirekapp.Classes.Supplier;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddTransactionActivity extends AppCompatActivity {

    RecyclerView rvProducts, rvContacts;
    ArrayList<Product> productArrayList;
    ArrayList<DataSnapshot> supplierArrayList;
    ArrayList<DataSnapshot> customerArrayList;
    ArrayList<String> products_id;
    FirebaseDatabaseHelper dbHelper;
    ProductRecyclerViewAdapter productRecyclerViewAdapter;
    ContactRecyclerViewAdapter contactRecyclerViewAdapter;

    EditText etQuantity;
    TextView tvDate;
    Button btnAddTransaction;

    long selectedDate;
    Product selectedProduct;
    Customer selectedCustomer;
    Supplier selectedSupplier;

    final int TRANSACTION_BUY = 0;
    final int TRANSACTION_SELL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        rvProducts = findViewById(R.id.rvProducts);
        rvContacts = findViewById(R.id.rvContacts);
        tvDate = findViewById(R.id.tvDate);
        etQuantity = findViewById(R.id.etQuantity);
        btnAddTransaction = findViewById(R.id.btnAddTrans);

        productArrayList = new ArrayList<>();
        products_id = new ArrayList<>();
        customerArrayList = new ArrayList<>();
        supplierArrayList = new ArrayList<>();

        dbHelper = new FirebaseDatabaseHelper();

        Intent intent = getIntent();
        final int transaction_type = intent.getIntExtra("type", -1);

        rvProducts.setHasFixedSize(true);
        rvContacts.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(AddTransactionActivity.this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(AddTransactionActivity.this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);

        rvProducts.setLayoutManager(layoutManager1);
        rvContacts.setLayoutManager(layoutManager2);

//        selectedProduct = productRecyclerViewAdapter.getSelectedProduct();
//        if (transaction_type == TRANSACTION_BUY) {
//            selectedSupplier = contactRecyclerViewAdapter.getSelectedContact().getValue(Supplier.class);
//        }
//        else if (transaction_type == TRANSACTION_SELL) {
//            selectedCustomer = contactRecyclerViewAdapter.getSelectedContact().getValue(Customer.class);
//        }
//        else {
//            Toast.makeText(AddTransactionActivity.this, "Inappropriate Access", Toast.LENGTH_SHORT).show();
//            finish();
//        }

        btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    selectedProduct = productRecyclerViewAdapter.getSelectedProduct();
                    final String selectedProductId = productRecyclerViewAdapter.getSelectedProductId();
                    final int quantity = Integer.parseInt(etQuantity.getText().toString());

                    if (transaction_type == TRANSACTION_BUY) {
                        if (!dbHelper.checkEnoughBalance(quantity * selectedProduct.getBuy_price())) {
                            Toast.makeText(AddTransactionActivity.this, "Not enough balance! Please check your balance.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        selectedSupplier = contactRecyclerViewAdapter.getSelectedContact().getValue(Supplier.class);
                        BuyTrans bt = new BuyTrans(selectedDate, selectedProduct.getBuy_price() * quantity, quantity, selectedProduct, selectedSupplier);
                        dbHelper.retrieveReference("Transactions").child("Buy").push().setValue(bt).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddTransactionActivity.this, "Successfully added buy transaction", Toast.LENGTH_SHORT).show();
                                selectedProduct.setQuantity(selectedProduct.getQuantity() + quantity);
                                dbHelper.retrieveReference("Products").child(selectedProductId).setValue(selectedProduct);
                                dbHelper.updateUserBalance(selectedProduct.getBuy_price() * quantity, TRANSACTION_BUY);
                                finish();
                            }
                        });
                    } else if (transaction_type == TRANSACTION_SELL) {
                        if (!dbHelper.checkEnoughQuantity(selectedProduct.getQuantity(), quantity)) {
                            Toast.makeText(AddTransactionActivity.this, "Not enough product quantity! Please check again.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        selectedCustomer = contactRecyclerViewAdapter.getSelectedContact().getValue(Customer.class);
                        SellTrans st = new SellTrans(selectedDate, selectedProduct.getSell_price() * quantity, quantity, selectedProduct, selectedCustomer);
                        dbHelper.retrieveReference("Transactions").child("Sell").push().setValue(st).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddTransactionActivity.this, "Successfully added sell transaction", Toast.LENGTH_SHORT).show();
                                selectedProduct.setQuantity(selectedProduct.getQuantity() - quantity);
                                dbHelper.retrieveReference("Products").child(selectedProductId).setValue(selectedProduct);
                                dbHelper.updateUserBalance(selectedProduct.getSell_price() * quantity, TRANSACTION_SELL);
                                dbHelper.updateCustomerTransactionNo(contactRecyclerViewAdapter.getSelectedContact().getKey(), selectedCustomer);
                                finish();
                            }
                        });
                    }
                } else {
                    Toast.makeText(AddTransactionActivity.this, "Please check if all the fields are filled.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddTransactionActivity.this,
                        R.style.Theme_AppCompat_DayNight_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tvDate.setText("" + (month + 1) + "/" + day + "/" + year);
                                Calendar newDate = new GregorianCalendar(year, month, day);
                                selectedDate = newDate.getTimeInMillis();
                            }
                        },
                        year, month, day
                );
                dialog.show();
            }
        });

        if (transaction_type == TRANSACTION_BUY) {
            dbHelper.retrieveReference("Contacts").child("Suppliers").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    supplierArrayList.add(dataSnapshot);
                    contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(AddTransactionActivity.this, supplierArrayList, customerArrayList);
                    rvContacts.setAdapter(contactRecyclerViewAdapter);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            dbHelper.retrieveReference("Contacts").child("Customers").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    customerArrayList.add(dataSnapshot);
                    contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(AddTransactionActivity.this, supplierArrayList, customerArrayList);
                    rvContacts.setAdapter(contactRecyclerViewAdapter);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        dbHelper.retrieveReference("Products").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                productArrayList.add(dataSnapshot.getValue(Product.class));
                products_id.add(dataSnapshot.getKey());

                productRecyclerViewAdapter = new ProductRecyclerViewAdapter(AddTransactionActivity.this, productArrayList, products_id);
                rvProducts.setAdapter(productRecyclerViewAdapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean validate() {
        return !etQuantity.getText().toString().isEmpty()
                && selectedDate != 0
                && contactRecyclerViewAdapter.getSelectedContact() != null
                && productRecyclerViewAdapter.getSelectedProduct() != null;
    }
}
