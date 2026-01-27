package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.Validator;

public class AdminEditUserActivity extends AppCompatActivity {
    String fName, lName, email, password, id;
    EditText tfName, tlName, temail, tpassword;
    Button back, Submit;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_user);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();

        fName = getIntent().getStringExtra("firstname");
        lName = getIntent().getStringExtra("lastname");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        id = getIntent().getStringExtra("id");

        tfName = findViewById(R.id.set_fname);
        tlName = findViewById(R.id.set_lname);
        temail = findViewById(R.id.set_email);
        tpassword = findViewById(R.id.set_password);

        // Populate fields
        tfName.setText(fName);
        tlName.setText(lName);
        temail.setText(email);
        tpassword.setText(password);

        back = findViewById(R.id.btnEditBack);
        Submit = findViewById(R.id.btnSaveUser);

        back.setOnClickListener(v -> {
            finish();
        });

        Submit.setOnClickListener(v -> {
            String newEmail = temail.getText().toString().trim();
            String newPass = tpassword.getText().toString().trim();
            String newFName = tfName.getText().toString().trim();
            String newLName = tlName.getText().toString().trim();

            if (!checkInput(newEmail, newPass, newFName, newLName)) {
                return;
            }

            databaseService.getUser(id, new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    if (user != null) {
                        user.setEmail(newEmail);
                        user.setPassword(newPass);
                        user.setFirstname(newFName);
                        user.setLastname(newLName);

                        databaseService.setUser(user, new DatabaseService.DatabaseCallback<Void>() {
                            @Override
                            public void onCompleted(Void object) {
                                Toast.makeText(AdminEditUserActivity.this, "Update Success!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailed(Exception e) {
                                Toast.makeText(AdminEditUserActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(AdminEditUserActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            });
        });
    } // Removed the extra closing bracket that was here

    private boolean checkInput(String email, String password, String fName, String lName) {
        if (!Validator.isEmailValid(email)) {
            temail.setError("Invalid email address");
            temail.requestFocus();
            return false;
        }
        if (!Validator.isPasswordValid(password)) {
            tpassword.setError("Password too short");
            tpassword.requestFocus();
            return false;
        }
        if (!Validator.isNameValid(fName)) {
            tfName.setError("Name too short");
            tfName.requestFocus();
            return false;
        }
        if (!Validator.isNameValid(lName)) {
            tlName.setError("Name too short");
            tlName.requestFocus();
            return false;
        }
        return true;
    }
}