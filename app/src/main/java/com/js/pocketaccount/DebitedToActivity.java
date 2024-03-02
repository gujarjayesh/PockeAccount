package com.js.pocketaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DebitedToActivity extends AppCompatActivity {

    int mYear,mMonth,mDay;
    TextInputEditText name,amount,date,narration;
    CardView adDebitedButton;
    FirebaseDatabase firebase;
    DatabaseReference databaseReferenceAccounts;
    SharedPreferences userDetails;
    String UserNo;
    ProgressBar progressDebit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debited_to);

        name = findViewById(R.id.nameDebited);
        amount = findViewById(R.id.amountDebited);
        date = findViewById(R.id.dateDebited);
        narration = findViewById(R.id.narrationDebited);
        adDebitedButton = findViewById(R.id.adDebitButton);
        progressDebit = findViewById(R.id.progressDebit);

        userDetails = getSharedPreferences("userDetail",0);

        UserNo = userDetails.getString("userNo","");
        firebase = FirebaseDatabase.getInstance();
        databaseReferenceAccounts = firebase.getReference("User").child("Accounts");
        adDebitedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals("") && !amount.getText().toString().equals("") && !date.getText().toString().equals("") && !narration.getText().toString().equals("") )
                {
                    progressDebit.setVisibility(View.VISIBLE);
                    SaveDetails();
                }
                else
                    Toast.makeText(DebitedToActivity.this, "Please Fill all Filed", Toast.LENGTH_SHORT).show();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(DebitedToActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                databaseReferenceAccounts.child(UserNo).child("Debited").child(key).child("name").setValue(name.getText().toString());
                databaseReferenceAccounts.child(UserNo).child("Debited").child(key).child("date").setValue(date.getText().toString().replace("/","-"));
                databaseReferenceAccounts.child(UserNo).child("Debited").child(key).child("amount").setValue(amount.getText().toString());
                databaseReferenceAccounts.child(UserNo).child("Debited").child(key).child("narration").setValue(narration.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DebitedToActivity.this, "Successfully Added Details", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        date.setText("");
                        amount.setText("");
                        narration.setText("");
                        progressDebit.setVisibility(View.GONE);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDebit.setVisibility(View.GONE);

            }
        });
    }
}