package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;

public class LandingActivity extends AppCompatActivity {
    Button BtnReg, BtnLog, BtnGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (SharedPreferencesUtil.isUserLoggedIn(this)) {
            Intent mainIntent = new Intent(LandingActivity.this, MainActivity.class);
            /// Clear the back stack (clear history) and start the MainActivity
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
        }
        BtnGuest = findViewById(R.id.btn_main_guest);
        BtnLog = findViewById(R.id.btn_main_login);
        BtnReg = findViewById(R.id.btn_main_register);
        BtnReg.setOnClickListener(v -> {
            Intent intentReg = new Intent(LandingActivity.this, RegisterActivity.class);
            startActivity(intentReg);
        });
        BtnGuest.setOnClickListener(v -> {
            Intent intentGuest = new Intent(LandingActivity.this, MainActivity.class);
            startActivity(intentGuest);
        });
        BtnLog.setOnClickListener(v -> {
            Intent intentLog = new Intent(LandingActivity.this, LoginActivity.class);
            startActivity(intentLog);
        });
        BtnGuest.setOnClickListener(v -> {
            Intent intentGuest = new Intent(LandingActivity.this, MainActivity.class);
            startActivity(intentGuest);
        });
    }
}