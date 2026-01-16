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

public class ViewApplicationActivity extends AppCompatActivity {
    TextView tvContent, fName, lName, Subject;
    Button BtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_application);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String content = getIntent().getStringExtra("content");
        String subject = getIntent().getStringExtra("subject");
        String fname = getIntent().getStringExtra("fname");
        String lname = getIntent().getStringExtra("lname");
        tvContent = findViewById(R.id.tv_application);
        tvContent.setText(content);
        Subject = findViewById(R.id.tv_item_subject);
        Subject.setText(subject);
        fName = findViewById(R.id.tv_item_user_fname);
        fName.setText(fname);
        lName = findViewById(R.id.tv_item_user_lname);
        lName.setText(lname);
        BtnBack = findViewById(R.id.btn_from_view_content_to_apply_list);
        BtnBack.setOnClickListener(v -> {
            finish();
        });
    }
}