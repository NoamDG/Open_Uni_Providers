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
    EditText TenSubject, ExpD, pubD;
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
        databaseService = DatabaseService.getInstance();
        String Winner = "";
        ExpD = findViewById(R.id.ExpDate);
        pubD = findViewById(R.id.PubDate);
        btnContent = findViewById(R.id.btnContent);
        btnBack = findViewById(R.id.btn_back_in_create_tender);
        btnBack.setOnClickListener(v -> {
            Intent back = new Intent(CreateTenderActivity.this, TenderActivity.class);
            startActivity(back);
        });
        tenderNum = DatabaseService.getInstance().generateTenderId();
        btnContent.setOnClickListener(v -> {

            if(!checkInputTender(TenSubject.getText().toString(), ExpD.getText().toString(), pubD.getText().toString())){
                return;
            }
            Intent intentContent = new Intent(CreateTenderActivity.this, TenderContentCreateActivity.class);
            intentContent.putExtra("Sub", TenSubject.getText().toString());
            intentContent.putExtra("ExpD", ExpD.getText().toString());
            intentContent.putExtra("Status", Status);
            intentContent.putExtra("Winner", Winner);
            intentContent.putExtra("PubD", pubD.getText().toString());
            startActivity(intentContent);
        });



    }
    private boolean checkInputTender(String subject, String ExpDa, String PubDa) {

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


























