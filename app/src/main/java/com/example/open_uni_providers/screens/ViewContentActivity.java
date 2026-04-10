package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    TextView tvContent, tvSubject, tvPublish, tvExpire, tvStatus, tvCategory, tvWinner;
    Button btnApps, BtnApply;
    ImageButton BtnBack;
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
        String content = getIntent().getStringExtra("content");
        String subject = getIntent().getStringExtra("subject");
        String publish = getIntent().getStringExtra("publish");
        String expire = getIntent().getStringExtra("expire");
        String category = getIntent().getStringExtra("category");
        String status = getIntent().getStringExtra("status");
        String winner = getIntent().getStringExtra("winner");
        String ID = getIntent().getStringExtra("id");
        user = SharedPreferencesUtil.getUser(ViewContentActivity.this);
        tvSubject = findViewById(R.id.tv_subject);
        tvSubject.setText(subject);
        tvPublish = findViewById(R.id.tv_publish_date);
        tvPublish.setText(publish);
        tvExpire = findViewById(R.id.tv_expire_date);
        tvExpire.setText(expire);
        tvStatus = findViewById(R.id.tv_status);
        tvStatus.setText(status);
        tvCategory = findViewById(R.id.tv_category);
        tvCategory.setText(category);
        tvWinner = findViewById(R.id.tv_winner_name);
        tvWinner.setText(winner);
        tvContent = findViewById(R.id.tv_content);
        tvContent.setText(content);
        BtnApply = findViewById(R.id.btn_tender_apply);
        btnApps = findViewById(R.id.btn_tender_view_app);
        if (user != null) {
            if (user.isEmployee()) {
                BtnApply.setVisibility(View.GONE);
                btnApps.setVisibility(View.VISIBLE);
            } else {
                if (status.equals("Active")) {
                    BtnApply.setVisibility(View.VISIBLE);
                } else {
                    BtnApply.setVisibility(View.GONE);
                }
                btnApps.setVisibility(View.GONE);
            }
        } else {
            btnApps.setVisibility(View.GONE);
            BtnApply.setVisibility(View.GONE);
        }
        btnApps.setOnClickListener(v -> {
            Intent apps = new Intent(ViewContentActivity.this, ApplyListActivity.class);
            apps.putExtra("subject", subject);
            apps.putExtra("id", ID);
            startActivity(apps);
        });
        BtnApply.setOnClickListener(v -> {
            Intent apply = new Intent(ViewContentActivity.this, ApplyActivity.class);
            apply.putExtra("subject", subject);
            startActivity(apply);
        });
        BtnBack = findViewById(R.id.btn_tender_content_back);
        BtnBack.setOnClickListener(v -> {
            finish();
        });
    }
}