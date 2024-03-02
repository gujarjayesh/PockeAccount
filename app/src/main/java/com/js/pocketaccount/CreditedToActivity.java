package com.js.pocketaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreditedToActivity extends AppCompatActivity {

    int mYear,mMonth,mDay;
    TextInputEditText name,amount,date,narration;

    CardView adCreditButton;
    FirebaseDatabase firebase;
    DatabaseReference databaseReferenceAccounts;
    SharedPreferences userDetails;
    String UserNo;
    ProgressBar progressCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_credited_to);

        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        narration = findViewById(R.id.narration);
        adCreditButton = findViewById(R.id.adCreditButton);
        progressCredit = findViewById(R.id.progressCredit);


        userDetails = getSharedPreferences("userDetail",0);

        UserNo = userDetails.getString("userNo","");
        firebase = FirebaseDatabase.getInstance();
        databaseReferenceAccounts = firebase.getReference("User").child("Accounts");
        adCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals("") && !amount.getText().toString().equals("") && !date.getText().toString().equals("") && !narration.getText().toString().equals("") )
                {
                    progressCredit.setVisibility(View.VISIBLE);
                    SaveDetails();
                }
                else
                    Toast.makeText(CreditedToActivity.this, "Please Fill all Filed", Toast.LENGTH_SHORT).show();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(CreditedToActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                        date.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

    }

    private void SaveDetails()
    {

        databaseReferenceAccounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReferenceAccounts.removeEventListener(this);
                String key = databaseReferenceAccounts.push().getKey();
                databaseReferenceAccounts.child(UserNo).child("Credits").child(key).child("name").setValue(name.getText().toString());
                databaseReferenceAccounts.child(UserNo).child("Credits").child(key).child("date").setValue(date.getText().toString().replace("/","-"));
                databaseReferenceAccounts.child(UserNo).child("Credits").child(key).child("amount").setValue(amount.getText().toString());
                databaseReferenceAccounts.child(UserNo).child("Credits").child(key).child("narration").setValue(narration.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreditedToActivity.this, "Successfully Added Details", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        date.setText("");
                        amount.setText("");
                        narration.setText("");
                        progressCredit.setVisibility(View.GONE);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressCredit.setVisibility(View.GONE);

            }
        });
    }
}