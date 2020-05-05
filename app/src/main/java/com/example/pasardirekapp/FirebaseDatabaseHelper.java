package com.example.pasardirekapp;

import androidx.annotation.NonNull;

import com.example.pasardirekapp.Classes.Customer;
import com.example.pasardirekapp.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FirebaseDatabaseHelper {
    private FirebaseAuth auth;
    private DatabaseReference DBref;
    private FirebaseUser firebaseUser;
    private SimpleDateFormat dateFormat;
    private User user;
    private Customer customer;
    private int balance;

    public FirebaseDatabaseHelper() {
        auth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference();
        firebaseUser = auth.getCurrentUser();
        retrieveReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    }

    public String getUID() {
        return firebaseUser.getUid();
    }

    public DatabaseReference retrieveReference(String ref_node) {
        return DBref.child(ref_node).child(getUID());
    }

    public String convertDateToString(long date) {
        return dateFormat.format(new Date(date));
    }

    public boolean checkEnoughBalance(int amount) {
        return user.getBalance() >= amount;
    }

    public boolean checkEnoughQuantity(int product_quantity, int sell_quantity) {
        return product_quantity >= sell_quantity;
    }

    public void updateUserBalance(int amount, int transactionType) {

        if (transactionType == 0) {
            user.setBalance(user.getBalance() - amount);
            user.setProfit(user.getBalance() - user.getInit_balance());
        } else {
            user.setBalance(user.getBalance() + amount);
            user.setRevenue(user.getRevenue() + amount);
            user.setProfit(user.getBalance() - user.getInit_balance());
        }
        retrieveReference("Users").setValue(user);
    }

    public void updateCustomerTransactionNo(String customer_key, Customer customer) {
        customer.setTrans_no(customer.getTrans_no() + 1);
        if (customer.getTrans_no() >= 40) {
            customer.setGrade(3);
        } else {
            customer.setGrade((customer.getTrans_no() / 10));
        }
        retrieveReference("Contacts").child("Customers").child(customer_key).setValue(customer);
    }

    public void signOut() {
        auth.signOut();
    }
}
