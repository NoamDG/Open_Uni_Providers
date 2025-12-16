package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;

public class ViewContentActivity extends AppCompatActivity {
    TextView tvContent;
    Button BtnBack;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String content_emp = getIntent().getStringExtra("content_emp");
        String content_pro = getIntent().getStringExtra("content_pro");
        /// put content in here by intent from the TenderActivity page
        tvContent = findViewById(R.id.tv_content);
        user = SharedPreferencesUtil.getUser(this);
        if(user.isEmployee()){
            tvContent.setText(content_emp);
        }
        else{
            tvContent.setText(content_pro);
        }
        BtnBack = findViewById(R.id.btn_from_view_content_to_tender);
        BtnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ViewContentActivity.this, TenderActivity.class);
            startActivity(intent);
        });
    }
}