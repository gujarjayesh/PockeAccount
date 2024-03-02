package com.js.pocketaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView signupText;
    Button loginButton;
    EditText phoneNumberLogin,password;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceUser;
    SharedPreferences userDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupText = findViewById(R.id.signupText);
        loginButton = findViewById(R.id.loginButton);
        password = findViewById(R.id.password);
        phoneNumberLogin = findViewById(R.id.phoneNumberLogin);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("User");

        userDetails = getSharedPreferences("userDetail",0);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phoneNumberLogin.getText().toString().equals("") && !password.getText().toString().equals(""))
                {
                    LoginUser();
                }
                else
                    Toast.makeText(LoginActivity.this, "Please Fill all Filed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void LoginUser()
    {
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReferenceUser.removeEventListener(this);
                if (snapshot.child("login").child(phoneNumberLogin.getText().toString()).exists())
                {
                    String passDB = snapshot.child("login").child(phoneNumberLogin.getText().toString()).child("password").getValue().toString();
                    if (passDB.equals(password.getText().toString()))
                    {
                        userDetails.edit().putString("userNo",phoneNumberLogin.getText().toString()).apply();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(LoginActivity.this, "No Such User with "+phoneNumberLogin.getText().toString()+" Number", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}