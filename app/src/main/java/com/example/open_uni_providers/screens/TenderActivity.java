package com.example.open_uni_providers.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.Tender;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;

import java.util.ArrayList;

public class TenderActivity extends AppCompatActivity {
    User user;
    Button BtnCreateTender;
    boolean isEmployee=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tender);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = SharedPreferencesUtil.getUser(TenderActivity.this);
        isEmployee = user.isEmployee();
        BtnCreateTender = findViewById(R.id.btn_create_tender);

        ArrayList<Tender> tenderList = new ArrayList<>();

        if (isEmployee) {
            BtnCreateTender.setVisibility(View.VISIBLE);
        }
        else {
            BtnCreateTender.setVisibility(View.GONE);  // hides it fully
        }
    }
}