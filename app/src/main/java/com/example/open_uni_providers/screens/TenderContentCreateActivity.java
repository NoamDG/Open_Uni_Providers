package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.Tender;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.Validator;

public class TenderContentCreateActivity extends AppCompatActivity {
    static final String TAG = "TenderContentCreateActivity";
    EditText content;
    Button btnSubmit;
    ImageButton BtnBack;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tender_content_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseService = DatabaseService.getInstance();
        String sub = getIntent().getStringExtra("Sub");
        String ExpD = getIntent().getStringExtra("ExpD");
        String Status = getIntent().getStringExtra("Status");
        String Winner = getIntent().getStringExtra("Winner");
        String PubD = getIntent().getStringExtra("PubD");
        String cat = getIntent().getStringExtra("category");
        content = findViewById(R.id.content_text);
        BtnBack = findViewById(R.id.btn_create_content_back);
        BtnBack.setOnClickListener(v -> {
            finish();
        });
        btnSubmit = findViewById(R.id.btn_submit_create_tender_content);
        btnSubmit.setOnClickListener(v -> {

            Log.d(TAG, "onClick: Tender Subject: " + sub);
            Log.d(TAG, "onClick: Expire Date: " + ExpD);
            Log.d(TAG, "onClick: Status: " + Status);
            Log.d(TAG, "onClick: Winner Name: " + Winner);
            Log.d(TAG, "onClick: Publish Date: " + PubD);
            Log.d(TAG, "onClick: Category: " + cat);
            Log.d(TAG, "onClick: Content: " + content.getText().toString());
            Log.d(TAG, "onClick: Validating input...");
            if (!checkInputTenderContent(content.getText().toString())) {
                /// stop if input is invalid
                return;
            }
            addTender(sub, ExpD, Status, Winner, PubD, cat, content.getText().toString());

            Log.d(TAG, "onClick: Adding tender...");
            Intent back_from_create_tender = new Intent(TenderContentCreateActivity.this, TenderActivity.class);
            startActivity(back_from_create_tender);
        });
    }

    private boolean checkInputTenderContent(String Content) {

        if (!Validator.isContentValid(Content)) {
            Log.e(TAG, "checkInput: must be above 20 characters");
            /// show error message to user
            content.setError("content must be above 20 characters");
            /// set focus to content field
            content.requestFocus();
            return false;
        }

        Log.d(TAG, "checkInput: Input is valid");
        return true;
    }

    private void addTender(String subject, String ExpD, String Status, String Winner, String PubD, String category, String content) {
        Log.d(TAG, "addTender: Adding tender...");
        String id = databaseService.generateTenderId();
        Tender tender = new Tender(id, subject, ExpD, Status, Winner, PubD, category, content);
        /// create a new user object
        createTenderInDatabase(tender);
    }

    private void createTenderInDatabase(Tender tender) {
        databaseService.setTender(tender, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "createTenderInDatabase: Redirecting to TenderActivity");
                /// Redirect to TenderActivity and clear back stack to prevent user from going back to create-tender screen
                Intent mainIntent = new Intent(TenderContentCreateActivity.this, TenderActivity.class);
                /// clear the back stack (clear history) and start the TenderActivity
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "createTenderInDatabase: Failed to create tender", e);
                /// show error message to user
                Toast.makeText(TenderContentCreateActivity.this, "Failed to create tender", Toast.LENGTH_SHORT).show();
            }
        });
    }
}