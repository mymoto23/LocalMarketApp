package com.example.pasardirekapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasardirekapp.R;
import com.example.pasardirekapp.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userEmail, userPassword, userPasswordConfirmation, userName, userCompany, userBalance;
    private Button SignUp;
    private TextView Click2SignIn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setVariables();

        firebaseAuth = firebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

        Click2SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    final String email = userEmail.getText().toString().trim();
                    String password = userPassword.getText().toString().trim();
                    final String name = userName.getText().toString().trim();
                    final String store = userCompany.getText().toString().trim();
                    final int balance = Integer.parseInt(userBalance.getText().toString());

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser fUser = firebaseAuth.getCurrentUser(); // get currently authenticated user object
                                User user = new User(name, email, store, balance);  //create new user Object
                                ref.child("Users").child(fUser.getUid()).setValue(user); //Under Users child, add user object
                                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Registration Fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void setVariables() {
        userBalance = (EditText) findViewById(R.id.etUserBalance);
        userName = (EditText) findViewById(R.id.etUserName);
        userCompany = (EditText) findViewById(R.id.etUserCompany);
        userEmail = (EditText)findViewById(R.id.etUserEmail);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        userPasswordConfirmation = (EditText)findViewById(R.id.etUserPasswordConfirmation);
        Click2SignIn = (TextView) findViewById(R.id.tvClick2SignIn);
        SignUp = (Button) findViewById(R.id.btSignUp);
    }

    public boolean validate() {
        Boolean completeInfo = false;

        String email = userEmail.getText().toString();
        String password1 = userPassword.getText().toString();
        String password2 = userPasswordConfirmation.getText().toString();

        if (email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else if (!(email.contains("@") & email.contains(".com"))) {
            Toast.makeText(this, "Please enter an appropriate email address", Toast.LENGTH_SHORT).show();
        } else if (!password1.equals(password2)) {
            Toast.makeText(this, "Please make sure passwords match", Toast.LENGTH_SHORT).show();
        } else {
            completeInfo = true;
        }

        return completeInfo;
    }
}
