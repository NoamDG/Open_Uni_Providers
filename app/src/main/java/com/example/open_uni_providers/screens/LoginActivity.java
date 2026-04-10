package com.example.open_uni_providers.screens;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;
import com.example.open_uni_providers.utils.Validator;

public class LoginActivity extends AppCompatActivity {
    static final String TAG = "LoginActivity";
    EditText tEmail, tPassword;
    Button BtnSubmitLog;
    ImageButton BtnLogBack;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BtnLogBack = findViewById(R.id.btn_login_back);
        BtnSubmitLog = findViewById(R.id.btn_submit_login);
        databaseService = DatabaseService.getInstance();
        tEmail = findViewById(R.id.inputEmail);
        tPassword = findViewById(R.id.inputPassword);
        BtnLogBack.setOnClickListener(v -> {
            finish();
        });
        BtnSubmitLog.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Login Button Clicked");
            String email = tEmail.getText().toString();
            String password = tPassword.getText().toString();
            Log.d(TAG, "onClick: Email: " + email);
            Log.d(TAG, "onClick: Password: " + password);
            Log.d(TAG, "onClick: Validating input...");
            if (!checkInput(email, password)) {
                /// stop if input is invalid
                return;
            }
            Log.d(TAG, "onClick: Logging in user...");
            loginUser(email, password);
        });

    }

    private boolean checkInput(String email, String password) {
        if (!Validator.isEmailValid(email)) {
            Log.e(TAG, "checkInput: Invalid email address");
            /// show error message to user
            tEmail.setError("Invalid email address");
            /// set focus to email field
            tEmail.requestFocus();
            return false;
        }


        return true;

    }

    private void loginUser(String email, String password) {
        databaseService.getUserByEmailAndPassword(email, password, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                if (user == null) {
                    /// Show error message to user
                    tPassword.setError("User doesn't exist");
                    tPassword.requestFocus();
                    return;
                }
                Log.d(TAG, "onCompleted: User logged in: " + user.toString());
                /// save the user data to shared preferences
                SharedPreferencesUtil.saveUser(LoginActivity.this, user);
                /// Redirect to main activity and clear back stack to prevent user from going back to login screen
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                /// Clear the back stack (clear history) and start the MainActivity
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
            }

            @Override
            public void onFailed(Exception e) {

                Log.e(TAG, "onFailed: Failed to retrieve user data", e);
                /// Show error message to user
                tPassword.setError("Invalid email or password");
                tPassword.requestFocus();
                /// Sign out the user if failed to retrieve user data
                /// This is to prevent the user from being logged in again
                SharedPreferencesUtil.signOutUser(LoginActivity.this);
            }
        });
    }
}