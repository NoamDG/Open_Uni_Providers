package com.example.open_uni_providers.screens;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.Validator;

public class CreateTenderActivity extends AppCompatActivity {
    static final String TAG = "CreateTenderActivity";
    EditText TenSubject, ExpD, TenCategory;
    String tenderNum;
    String tContent="";
    Button btnContent, btnSubmit, btnBack;

    String tenderId;
    DatabaseService databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_tender);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String Status="Active";
        TenSubject = findViewById(R.id.TenderSubject);
        TenCategory = findViewById(R.id.Category);
        databaseService = DatabaseService.getInstance();
        String Winner = "";
        ExpD = findViewById(R.id.ExpDate);

        ExpD.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            int year = cal.get(java.util.Calendar.YEAR);
            int month = cal.get(java.util.Calendar.MONTH);
            int day = cal.get(java.util.Calendar.DAY_OF_MONTH);

            android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                    CreateTenderActivity.this,
                    (view, y, m, d) -> {
                        // This creates the EXACT string "dd/MM/yyyy"
                        // %02d means: 2 digits, pad with zero if needed
                        String strDate = String.format(java.util.Locale.getDefault(), "%02d/%02d/%04d", d, m + 1, y);
                        /// note to self: the %02d - 2d means how many digits, the 0 before means that if its a singular characters theres a 0 at start.
                        /// note 2: the m+1 is because the android count for months starts at 0, so adding 1 makes it readable
                        ExpD.setText(strDate);
                    },
                    year, month, day
            );

            // Set minimum date to today so they can't pick the past
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        java.util.Date currentDate = calendar.getTime();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        String publishD =formattedDate;
        btnContent = findViewById(R.id.btnContent);
        btnBack = findViewById(R.id.btn_back_in_create_tender);
        btnBack.setOnClickListener(v -> {
            Intent back = new Intent(CreateTenderActivity.this, TenderActivity.class);
            startActivity(back);
        });
        tenderNum = DatabaseService.getInstance().generateTenderId();
        btnContent.setOnClickListener(v -> {

            if(!checkInputTender(TenSubject.getText().toString(), ExpD.getText().toString(), publishD, TenCategory.getText().toString())){
                return;
            }
            Intent intentContent = new Intent(CreateTenderActivity.this, TenderContentCreateActivity.class);
            intentContent.putExtra("Sub", TenSubject.getText().toString());
            intentContent.putExtra("ExpD", ExpD.getText().toString());
            intentContent.putExtra("Status", Status);
            intentContent.putExtra("Winner", Winner);
            intentContent.putExtra("PubD", publishD);
            intentContent.putExtra("category", TenCategory.getText().toString());
            startActivity(intentContent);
        });



    }
    private boolean checkInputTender(String subject, String ExpDa, String PubDa, String category) {

        if (!Validator.isSubjectValid(subject)) {
            Log.e(TAG, "checkInput: Invalid subject");
            /// show error message to user
            TenSubject.setError("Please enter subject");
            /// set focus to subject field
            TenSubject.requestFocus();
            return false;
        }
        if (!Validator.isCategoryValid(category)) {
            Log.e(TAG, "checkInput: Invalid category");
            /// show error message to user
            TenCategory.setError("Please enter category, must be without numbers");
            /// set focus to subject field
            TenCategory.requestFocus();
            return false;
        }
        if (!Validator.isDateValid(PubDa, ExpDa)) {
            Log.e(TAG, "checkInput: Expire Date must be later than today");
            /// show error message to user
            ExpD.setError("Expire Date must be later than today");
            /// set focus to publish date field
            ExpD.requestFocus();
            return false;
        }



        Log.d(TAG, "checkInput: Input is valid");
        return true;
    }


}


























