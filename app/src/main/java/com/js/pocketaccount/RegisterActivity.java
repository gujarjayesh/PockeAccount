package com.js.pocketaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText PhoneNumber,Username,Password;
    Button registerButton;
    FirebaseDatabase  firebaseDatabase;
    DatabaseReference databaseReferenceUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        PhoneNumber = findViewById(R.id.phoneNumber);
        Username = findViewById(R.id.usernameRegister);
        Password = findViewById(R.id.passwordRegister);
        registerButton = findViewById(R.id.registerButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("User");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PhoneNumber.getText().toString().equals("") && !Username.getText().toString().equals("") && !Password.getText().toString().equals(""))
                {
                    RegisterUser();
                }
                else
                    Toast.makeText(RegisterActivity.this, "Please Fill all Filed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void RegisterUser()
    {
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReferenceUser.removeEventListener(this);
                if (!snapshot.child("login").child(PhoneNumber.getText().toString()).exists())
                {
                    databaseReferenceUser.child("login").child(PhoneNumber.getText().toString()).child("no").setValue(PhoneNumber.getText().toString());
                    databaseReferenceUser.child("login").child(PhoneNumber.getText().toString()).child("password").setValue(Password.getText().toString());
                    databaseReferenceUser.child("login").child(PhoneNumber.getText().toString()).child("name").setValue(Username.getText().toString());
                    Toast.makeText(RegisterActivity.this, "Successfully Sign Up", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(RegisterActivity.this, "Already Exist User Please Sign In", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}