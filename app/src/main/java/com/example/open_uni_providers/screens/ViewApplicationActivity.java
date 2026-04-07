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
import com.example.open_uni_providers.models.Application;
import com.example.open_uni_providers.models.Tender;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;
import com.example.open_uni_providers.utils.Validator;

public class ViewApplicationActivity extends AppCompatActivity {
    TextView tvContent, fName, lName, Subject, Status;
    Button BtnAccept;
    ImageButton BtnBack;
    DatabaseService databaseService;
    User user;
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
        user = SharedPreferencesUtil.getUser(ViewApplicationActivity.this);
        String content = getIntent().getStringExtra("content");
        String subject = getIntent().getStringExtra("subject");
        String fname = getIntent().getStringExtra("fname");
        String lname = getIntent().getStringExtra("lname");
        String status = getIntent().getStringExtra("status");
        String AppID = getIntent().getStringExtra("idAPP");
        String ID = getIntent().getStringExtra("id");
        databaseService = DatabaseService.getInstance();
        BtnAccept = findViewById(R.id.btn_accept_app);
        tvContent = findViewById(R.id.tv_application);
        tvContent.setText(content);
        Subject = findViewById(R.id.tv_item_subject);
        Subject.setText(subject);
        fName = findViewById(R.id.tv_item_user_fname);
        fName.setText(fname);
        lName = findViewById(R.id.tv_item_user_lname);
        lName.setText(lname);
        String pWinner = fname +" "+lname;
        BtnBack = findViewById(R.id.btn_application_content_back);
        BtnBack.setOnClickListener(v -> {
            finish();
        });
        if(status.equals("PENDING") || user.isEmployee()){
            BtnAccept.setVisibility(View.VISIBLE);
        }
        if(status.equals("ACCEPTED") || !user.isEmployee()){
            BtnAccept.setVisibility(View.GONE);
        }
        BtnAccept.setOnClickListener(v -> {
            databaseService.getTender(ID, new DatabaseService.DatabaseCallback<Tender>() {
                @Override
                public void onCompleted(Tender tender) {
                    tender.setTenWinner(pWinner);
                    databaseService.setTender(tender, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void object) {

                        }

                        @Override
                        public void onFailed(Exception e) {

                        }
                    });
                    if(tender.getTenStat().equals("Active")){
                        tender.setTenStat("Inactive");
                    }
                    databaseService.setTender(tender, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void object) {

                        }

                        @Override
                        public void onFailed(Exception e) {

                        }
                    });
                    finish();
                }

                @Override
                public void onFailed(Exception e) {

                }
            });
            databaseService.getApplication(AppID, new DatabaseService.DatabaseCallback<Application>() {
                @Override
                public void onCompleted(Application application) {
                    application.setStatus("ACCEPTED");
                    databaseService.setApplication(application, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void object) {

                        }

                        @Override
                        public void onFailed(Exception e) {

                        }
                    });
                    finish();
                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        });

    }
}