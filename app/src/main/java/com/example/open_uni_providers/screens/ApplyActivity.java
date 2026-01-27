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
import com.example.open_uni_providers.models.Application;
import com.example.open_uni_providers.models.Tender;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;
import com.example.open_uni_providers.utils.Validator;

public class ApplyActivity extends AppCompatActivity {
    EditText content;
    Button back, submit;
    User user;
    DatabaseService databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_apply);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        content = findViewById(R.id.application);
        submit = findViewById(R.id.btn_submit_application);
        back = findViewById(R.id.btn_back_from_appeal_to_tender);
        user = SharedPreferencesUtil.getUser(ApplyActivity.this);
        databaseService = DatabaseService.getInstance();
        String subject = getIntent().getStringExtra("subject");
        back.setOnClickListener(v -> {
            finish();
        });
        submit.setOnClickListener(v -> {
            if (!checkInputApplication(content.getText().toString())) {
                /// stop if input is invalid
                return;
            }
            addApplication(user.getFirstname(), user.getLastname(), content.getText().toString(), subject);
            Intent back_from_create_application = new Intent(ApplyActivity.this, TenderActivity.class);
            startActivity(back_from_create_application);
        });

    }
    private boolean checkInputApplication(String Content) {

        if (!Validator.isContentValid(Content)) {
            /// show error message to user
            content.setError("content must be above 20 characters");
            /// set focus to content field
            content.requestFocus();
            return false;
        }

        return true;
    }
    private void addApplication(String fName, String lName, String content, String subject) {
        String id = databaseService.generateTenderId();
        Application application= new Application(id, fName, lName, content, subject);
        createApplicationInDatabase(application);
    }

    private void createApplicationInDatabase(Application application) {
        databaseService.setApplication(application, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                /// Redirect to TenderActivity and clear back stack to prevent user from going back to create-tender screen
                Intent mainIntent = new Intent(ApplyActivity.this, TenderActivity.class);
                /// clear the back stack (clear history) and start the TenderActivity
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(ApplyActivity.this, "Failed to create application", Toast.LENGTH_SHORT).show();
            }
        });
    }
}