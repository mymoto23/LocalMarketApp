package com.example.pasardirekapp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pasardirekapp.Activity.LoginActivity;
import com.example.pasardirekapp.Classes.BuyTrans;
import com.example.pasardirekapp.Classes.Contact;
import com.example.pasardirekapp.Classes.Customer;
import com.example.pasardirekapp.Classes.Product;
import com.example.pasardirekapp.Classes.Supplier;
import com.example.pasardirekapp.Classes.User;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class UserFragment extends Fragment {

    View view;
    Context context;
    private FirebaseDatabaseHelper dbHelper;
    TextView tvUserName, tvStoreName, tvInitialBalance, tvCurrentBalance, tvRevenue, tvProfit;
    Button signOut;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        context = view.getContext();

        dbHelper = new FirebaseDatabaseHelper();

        tvUserName = view.findViewById(R.id.tvUserName);
        tvStoreName = view.findViewById(R.id.tvStoreName);
        tvInitialBalance = view.findViewById(R.id.tvInitialBalance);
        tvCurrentBalance = view.findViewById(R.id.tvCurrentBalance);
        tvRevenue = view.findViewById(R.id.tvRevenue);
        tvProfit = view.findViewById(R.id.tvProfit);
        signOut = view.findViewById(R.id.signOut);

        dbHelper.retrieveReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvUserName.setText(user.getName());
                tvStoreName.setText(user.getStore());
                tvInitialBalance.setText("" + user.getInit_balance());
                tvCurrentBalance.setText("" + user.getBalance());
                tvRevenue.setText("" + user.getRevenue());
                tvProfit.setText("" + user.getProfit());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Sign Out")
                        .setMessage("Are you sure?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.signOut();
                                ((Activity)context).finish();
                                context.startActivity(new Intent(context, LoginActivity.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });


        return view;
    }
}
