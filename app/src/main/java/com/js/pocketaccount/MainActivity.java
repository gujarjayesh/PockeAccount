package com.js.pocketaccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    MaterialCardView creditedCardBtn,accountCardBtn,debitedCardBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        creditedCardBtn = findViewById(R.id.creditedCardBtn);
        accountCardBtn = findViewById(R.id.accountCardBtn);
        debitedCardBtn = findViewById(R.id.debitedCardBtn);

        creditedCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreditedToActivity.class);
                startActivity(intent);
            }
        });
        accountCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountsActivity.class);
                startActivity(intent);
            }
        });
        debitedCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DebitedToActivity.class);
                startActivity(intent);
            }
        });
    }
}