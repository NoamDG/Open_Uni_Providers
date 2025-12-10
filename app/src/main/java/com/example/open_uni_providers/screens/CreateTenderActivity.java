package com.example.open_uni_providers.screens;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.Tender;
import com.example.open_uni_providers.models.TenderContent;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;
import com.example.open_uni_providers.utils.Validator;

public class CreateTenderActivity extends AppCompatActivity {
    static final String TAG = "CreateTenderActivity";
    EditText TenSubject, Status, Winner, ExpD, pubD;
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
        TenSubject = findViewById(R.id.TenderSubject);
        databaseService = DatabaseService.getInstance();
        Status = findViewById(R.id.Status);
        ExpD = findViewById(R.id.ExpDate);
        pubD = findViewById(R.id.PubDate);
        Winner = findViewById(R.id.WinnerName);
        btnContent = findViewById(R.id.btnContent);
        btnBack = findViewById(R.id.btn_back_in_create_tender);
        tenderNum = DatabaseService.getInstance().generateTenderId();
        btnContent.setOnClickListener(v -> {

            if(!checkInputTender(TenSubject.getText().toString(), ExpD.getText().toString(), Status.getText().toString(), Winner.getText().toString(), pubD.getText().toString())){
                return;
            }
            Intent intentContent = new Intent(CreateTenderActivity.this, TenderContentCreateActivity.class);
            intentContent.putExtra("Sub", TenSubject.getText().toString());
            intentContent.putExtra("ExpD", ExpD.getText().toString());
            intentContent.putExtra("Status", Status.getText().toString());
            intentContent.putExtra("Winner", Winner.getText().toString());
            intentContent.putExtra("PubD", pubD.getText().toString());
            startActivity(intentContent);
        });



    }
    private boolean checkInputTender(String subject, String ExpDa, String status, String winner, String PubDa) {

        if (!Validator.isSubjectValid(subject)) {
            Log.e(TAG, "checkInput: Invalid subject");
            /// show error message to user
            TenSubject.setError("Please enter subject");
            /// set focus to subject field
            TenSubject.requestFocus();
            return false;
        }

        if (!Validator.isDateValid(ExpDa)) {
            Log.e(TAG, "checkInput: Expire Date must be in the format xx/xx/xxxx");
            /// show error message to user
            ExpD.setError("Expire Date must be in the format xx/xx/xxxx");
            /// set focus to ExpD field
            ExpD.requestFocus();
            return false;
        }

        if (!Validator.isStatusValid(status)) {
            Log.e(TAG, "checkInput: Status must be either: Active, Inactive, or Ended");
            /// show error message to user
            Status.setError("Status must be either: Active, Inactive, or Ended");
            /// set focus to status field
            Status.requestFocus();
            return false;
        }

        if (!Validator.isWinnerValid(winner)) {
            Log.e(TAG, "checkInput: Winner name must be at least 3 characters long");
            /// show error message to user
            Winner.setError("Last name must be at least 3 characters long or blank");
            /// set focus to winner name field
            Winner.requestFocus();
            return false;
        }

        if (!Validator.isDateValid(PubDa)) {
            Log.e(TAG, "checkInput: Expire Date must be in the format xx/xx/xxxx");
            /// show error message to user
            pubD.setError("Expire Date must be in the format xx/xx/xxxx");
            /// set focus to publish date field
            pubD.requestFocus();
            return false;
        }



        Log.d(TAG, "checkInput: Input is valid");
        return true;
    }


}


























