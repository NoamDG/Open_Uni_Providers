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

public class LandingActivity extends AppCompatActivity {
    Button BtnReg, BtnLog;
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
        BtnLog = findViewById(R.id.btn_main_login);
        BtnReg = findViewById(R.id.btn_main_register);
        BtnReg.setOnClickListener(v -> {
            Intent intentReg = new Intent(LandingActivity.this, RegisterActivity.class);
            startActivity(intentReg);
        });
        BtnLog.setOnClickListener(v -> {
            Intent intentLog = new Intent(LandingActivity.this, LoginActivity.class);
            startActivity(intentLog);
        });
    }
}