package com.example.open_uni_providers.screens;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class CreateTenderActivity extends AppCompatActivity {
    static final String TAG = "RegisterActivity";
    EditText TenSubject, Status, Winner, ExpD, pubD;
    String tenderNum;
    String content="";
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
        btnSubmit = findViewById(R.id.btn_submit_create_tender);
        btnBack = findViewById(R.id.btn_back_in_create_tender);
        tenderNum = DatabaseService.getInstance().generateTenderId();
        btnContent.setOnClickListener(v -> {
            Intent intentContent = new Intent(CreateTenderActivity.this, TenderContent.class);
            startActivity(intentContent);
        });
        String content = getIntent().getStringExtra("tenderCon");
        btnSubmit.setOnClickListener(v -> {
            Tender tender = new Tender(TenSubject.getText().toString(), ExpD.getText().toString(), Status.getText().toString(), Winner.getText().toString(), pubD.getText().toString(), content);
            Log.d(TAG, "onClick: Tender Subject: " + tender.getSubject());
            Log.d(TAG, "onClick: Expire Date: " + tender.getExpireDate());
            Log.d(TAG, "onClick: Status: " + tender.getStatus());
            Log.d(TAG, "onClick: Winner Name: " + tender.getWinnerName());
            Log.d(TAG, "onClick: Publish Date: " + tender.getPublish());
            Log.d(TAG, "onClick: Content: " + tender.getContent());
            Log.d(TAG, "onClick: Validating input...");
            if (!checkInputTender(tender.getSubject(), tender.getExpireDate(), tender.getStatus(), tender.getWinnerName(), tender.getPublish(),tender.getContent() )) {
                /// stop if input is invalid
                return;
            }
            addTender(tender.getSubject(), tender.getExpireDate(), tender.getStatus(), tender.getWinnerName(), tender.getPublish(), tender.getContent());

            Log.d(TAG, "onClick: Registering user...");
            Intent back_from_create_tender = new Intent(CreateTenderActivity.this, TenderActivity.class);
            startActivity(back_from_create_tender);
        });


    }
    private void addTender(String subject, String ExpD, String Status, String Winner, String PubD, String content) {
        Log.d(TAG, "registerUser: Registering user...");
        Tender tender = new Tender(subject, ExpD, Status, Winner, PubD, content);


        /// create a new user object
        createTenderInDatabase(tender);


    }

    private void createTenderInDatabase(Tender tender) {
        databaseService.createNewTender(tender, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "createUserInDatabase: Redirecting to TenderActivity");
                /// Redirect to TenderActivity and clear back stack to prevent user from going back to register screen
                Intent mainIntent = new Intent(CreateTenderActivity.this, TenderActivity.class);
                /// clear the back stack (clear history) and start the TenderActivity
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "createUserInDatabase: Failed to create tender", e);
                /// show error message to user
                Toast.makeText(CreateTenderActivity.this, "Failed to create tender", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


























