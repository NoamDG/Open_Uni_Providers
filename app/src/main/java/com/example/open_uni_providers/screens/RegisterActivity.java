package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.R;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;
import com.example.open_uni_providers.utils.Validator;

public class RegisterActivity extends AppCompatActivity {
    static final String TAG = "RegisterActivity";

    EditText tEmail, tPassword, tFName,tLName, tID;
    RadioGroup type;
    boolean isEmployee = false;
    Button btnSubmitReg, btnRegBack;
    DatabaseService databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RadioGroup type = findViewById(R.id.radioGroupRole);
        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedRoleId = type.getCheckedRadioButtonId();

                if (selectedRoleId == R.id.rBtnEmp) {
                    isEmployee = true;
                } else if (selectedRoleId == R.id.rBtnPro) {
                    isEmployee = false;
                }
            }
        });
        btnRegBack = findViewById(R.id.btn_register_to_landing);
        databaseService = DatabaseService.getInstance();
        tEmail = findViewById(R.id.user_Email);
        tPassword = findViewById(R.id.user_Password);
        tFName = findViewById(R.id.user_FirstName);
        tLName = findViewById(R.id.user_LastName);
        tID = findViewById(R.id.user_ID);
        btnSubmitReg = findViewById(R.id.btnSubmit);
        btnRegBack.setOnClickListener(v -> {
            finish();
        });
        btnSubmitReg.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Register button clicked");

            /// get the input from the user
            String email = tEmail.getText().toString();
            String password = tPassword.getText().toString();
            String fName = tFName.getText().toString();
            String lName = tLName.getText().toString();
            String id = tID.getText().toString();
            Log.d(TAG, "onClick: Email: " + email);
            Log.d(TAG, "onClick: Password: " + password);
            Log.d(TAG, "onClick: First Name: " + fName);
            Log.d(TAG, "onClick: Last Name: " + lName);
            Log.d(TAG, "onClick: ID: " + id);
            Log.d(TAG, "onClick: Validating input...");
            if (!checkInput(email, password, fName, lName, id)) {
                /// stop if input is invalid
                return;
            }

            Log.d(TAG, "onClick: Registering user...");

            /// Register user
            registerUser(email, password, fName, lName, id);
        });
    }
    private boolean checkInput(String email, String password, String fName, String lName, String id) {

        if (!Validator.isEmailValid(email)) {
            Log.e(TAG, "checkInput: Invalid email address");
            /// show error message to user
            tEmail.setError("Invalid email address");
            /// set focus to email field
            tEmail.requestFocus();
            return false;
        }

        if (!Validator.isPasswordValid(password)) {
            Log.e(TAG, "checkInput: Password must be at least 6 characters long");
            /// show error message to user
            tPassword.setError("Password must be at least 6 characters long");
            /// set focus to password field
            tPassword.requestFocus();
            return false;
        }

        if (!Validator.isNameValid(fName)) {
            Log.e(TAG, "checkInput: First name must be at least 3 characters long");
            /// show error message to user
            tFName.setError("First name must be at least 3 characters long");
            /// set focus to first name field
            tFName.requestFocus();
            return false;
        }

        if (!Validator.isNameValid(lName)) {
            Log.e(TAG, "checkInput: Last name must be at least 3 characters long");
            /// show error message to user
            tLName.setError("Last name must be at least 3 characters long");
            /// set focus to last name field
            tLName.requestFocus();
            return false;
        }

        if (!Validator.isIDValid(id)) {
            Log.e(TAG, "checkInput: ID must be 9 characters long");
            /// show error message to user
            tID.setError("ID must be 9 characters long");
            /// set focus to ID field
            tID.requestFocus();
            return false;
        }


        Log.d(TAG, "checkInput: Input is valid");
        return true;
    }

    /// Register the user
    private void registerUser(String email, String password, String fName, String lName, String ID) {
        Log.d(TAG, "registerUser: Registering user...");


        /// create a new user object
        User user = new User(ID, email, fName,lName, password, false,isEmployee );

        databaseService.checkIfEmailOrIDExists(email, ID, new DatabaseService.DatabaseCallback<Boolean>() {
            @Override
            public void onCompleted(Boolean exists) {
                if (exists) {
                    Log.e(TAG, "onCompleted: id or email already exists");
                    /// show error message to user
                    Toast.makeText(RegisterActivity.this, "id or email already exists", Toast.LENGTH_SHORT).show();
                } else {
                    /// proceed to create the user
                    createUserInDatabase(user);
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to check if email exists", e);
                /// show error message to user
                Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserInDatabase(User user) {
        databaseService.setUser(user, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "createUserInDatabase: User created successfully");
                /// save the user to shared preferences
                SharedPreferencesUtil.saveUser(RegisterActivity.this, user);
                Log.d(TAG, "createUserInDatabase: Redirecting to MainActivity");
                /// Redirect to MainActivity and clear back stack to prevent user from going back to register screen
                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                /// clear the back stack (clear history) and start the MainActivity
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "createUserInDatabase: Failed to create user", e);
                /// show error message to user
                Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                /// sign out the user if failed to register
                SharedPreferencesUtil.signOutUser(RegisterActivity.this);
            }
        });
    }
}