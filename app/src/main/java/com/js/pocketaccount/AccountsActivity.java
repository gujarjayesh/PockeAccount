package com.js.pocketaccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.js.pocketaccount.Adapters.AccountAdapter;
import com.js.pocketaccount.Models.Accounts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AccountsActivity extends AppCompatActivity {
    CardView DebitedBtn,CreditedBtn;
    TextView creditedTextView,debitedTextView,tabTextView;
    RecyclerView AccountRecyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceAccountsCredit;
    DatabaseReference databaseReferenceAccountsDebit;
    DatabaseReference databaseReferenceAccountsDebitSearch;
    SharedPreferences userDetails;
    AccountAdapter accountAdapter;
    LinearLayout pdfLayout;
    ExtendedFloatingActionButton createPdfBtn;
    String UserNo,selectedDateYear;
    Bitmap bitmap;
    int hours,minutes,seconds;
    ImageView filterBtn;
    Query query;
    private PdfGenerator.XmlToPDFLifecycleObserver xmlToPDFLifecycleObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        DebitedBtn = findViewById(R.id.DebitedBtn);
        CreditedBtn = findViewById(R.id.CreditedBtn);
        creditedTextView = findViewById(R.id.creditedTextView);
        debitedTextView = findViewById(R.id.debitedTextView);
        pdfLayout = findViewById(R.id.pdfLayout);
        createPdfBtn = findViewById(R.id.createPdfBtn);
        tabTextView = findViewById(R.id.tabTextView);
        filterBtn = findViewById(R.id.filterBtn);
        AccountRecyclerView = findViewById(R.id.AccountRecyclerView);
        userDetails = getSharedPreferences("userDetail",0);
        UserNo = userDetails.getString("userNo","");



        xmlToPDFLifecycleObserver = new PdfGenerator.XmlToPDFLifecycleObserver(this);
        getLifecycle().addObserver(xmlToPDFLifecycleObserver);
        tabTextView.setText("Debited Details");


        AccountRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceAccountsCredit = firebaseDatabase.getReference("User").child("Accounts").child(UserNo).child("Credits");
        databaseReferenceAccountsDebit = firebaseDatabase.getReference("User").child("Accounts").child(UserNo).child("Debited");
        FirebaseRecyclerOptions<Accounts> options = new FirebaseRecyclerOptions.Builder<Accounts>()
                .setQuery(databaseReferenceAccountsDebit,Accounts.class).build();

        accountAdapter = new AccountAdapter(options);
        AccountRecyclerView.setAdapter(accountAdapter);
        accountAdapter.startListening();
        accountAdapter.notifyDataSetChanged();

        DebitedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditedBtn.getBackground().setTint(Color.parseColor("#D9D9D9"));
                creditedTextView.setTextColor(Color.BLACK);
                DebitedBtn.getBackground().setTint(Color.RED);
                debitedTextView.setTextColor(Color.WHITE);
                tabTextView.setText("Debited Details");

                FirebaseRecyclerOptions<Accounts> options = new FirebaseRecyclerOptions.Builder<Accounts>()
                        .setQuery(databaseReferenceAccountsDebit,Accounts.class).build();

                accountAdapter = new AccountAdapter(options);
                AccountRecyclerView.setAdapter(accountAdapter);
                accountAdapter.startListening();
                accountAdapter.notifyDataSetChanged();
            }
        });

        CreditedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebitedBtn.getBackground().setTint(Color.parseColor("#D9D9D9"));
                debitedTextView.setTextColor(Color.BLACK);
                tabTextView.setText("Credited Details");

                CreditedBtn.getBackground().setTint(Color.parseColor("#118C4F"));
                creditedTextView.setTextColor(Color.WHITE);

                FirebaseRecyclerOptions<Accounts> options = new FirebaseRecyclerOptions.Builder<Accounts>()
                        .setQuery(databaseReferenceAccountsCredit,Accounts.class).build();

                accountAdapter = new AccountAdapter(options);
                AccountRecyclerView.setAdapter(accountAdapter);
                accountAdapter.startListening();
                accountAdapter.notifyDataSetChanged();

            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        createPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);
                //createPDF();
                Date dt = new Date();
                hours = dt.getHours();
                minutes = dt.getMinutes();
                seconds = dt.getSeconds();
                String hr = String.valueOf(hours);
                String min = String.valueOf(minutes);
                String sec = String.valueOf(seconds);
                PdfGenerator.getBuilder()
                        .setContext(AccountsActivity.this)
                        .fromViewSource()
                        .fromView(pdfLayout)
                        .setFileName(tabTextView.getText().toString()+" -PDF"+hr+" "+min+" "+sec)
                        .savePDFSharedStorage(xmlToPDFLifecycleObserver)
                        .setFolderNameOrPath("FolderC")
                        .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.OPEN)
                        .build(new PdfGeneratorListener() {
                            @Override
                            public void onFailure(FailureResponse failureResponse) {
                                super.onFailure(failureResponse);
                                Toast.makeText(AccountsActivity.this, failureResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("pdferror",failureResponse.getErrorMessage());
                            }

                            @Override
                            public void showLog(String log) {
                                super.showLog(log);
                            }

                            @Override
                            public void onStartPDFGeneration() {
                                /*When PDF generation begins to start*/
                            }

                            @Override
                            public void onFinishPDFGeneration() {
                                /*When PDF generation is finished*/
                                Toast.makeText(AccountsActivity.this, "generated", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(SuccessResponse response) {
                                super.onSuccess(response);
                                Toast.makeText(AccountsActivity.this, "Successfully saved", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void openDatePicker() {
        // Get the current date to set as the initial date in the date picker
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Do something with the selected date (e.g., update a TextView)
                        String month = String.valueOf(selectedMonth + 1);
                        if(selectedMonth + 1 < 10){
//                            Toast.makeText(AccountsActivity.this,"in less 10", Toast.LENGTH_SHORT).show();

                            month = null;
                            month = "0" + String.valueOf(selectedMonth + 1);
                        }
                         selectedDateYear = selectedDayOfMonth + "-" + (month) + "-" + selectedYear;
                         String filterDate = selectedDateYear.substring(8,10);
                         String selectedDate = selectedDayOfMonth + "-" + month +"-" + filterDate;
//                        Toast.makeText(AccountsActivity.this,selectedDayOfMonth + "-" + month +"-" + filterDate, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(AccountsActivity.this,selectedDate+  "  test", Toast.LENGTH_SHORT).show();
                        // Update your UI here
                        FirebaseRecyclerOptions<Accounts> options = new FirebaseRecyclerOptions.Builder<Accounts>()
                                .setQuery(databaseReferenceAccountsDebit.orderByChild("date").equalTo(selectedDate),Accounts.class).build();

                        accountAdapter = new AccountAdapter(options);
                        AccountRecyclerView.setAdapter(accountAdapter);
                        accountAdapter.startListening();
                        accountAdapter.notifyDataSetChanged();
                        if (accountAdapter.getItemCount() == 0)
                        {
                            Toast.makeText(AccountsActivity.this, "No Data Found for "+ selectedDate, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                year,
                month,
                day
        );

        // Show the date picker dialog

//        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "search", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                Toast.makeText(AccountsActivity.this, selectedDate, Toast.LENGTH_SHORT).show();
//
//            }
//        });
        datePickerDialog.show();
    }

}