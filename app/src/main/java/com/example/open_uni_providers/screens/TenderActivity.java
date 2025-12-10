package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.adapters.TenderAdapter;
import com.example.open_uni_providers.models.Tender;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class TenderActivity extends AppCompatActivity {
    User user;
    Button BtnCreateTender;
    boolean isEmployee=false;
    RecyclerView rvList;
    DatabaseService databaseService;
    TenderAdapter tenderAdapter;
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
        databaseService = DatabaseService.getInstance();
        user = SharedPreferencesUtil.getUser(TenderActivity.this);
        isEmployee = user.isEmployee();
        BtnCreateTender = findViewById(R.id.btn_create_tender);
        rvList = findViewById(R.id.rv_tender_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));

        tenderAdapter = new TenderAdapter(new TenderAdapter.OnTenderClickListener() {
            @Override
            public void onClick(Tender Tender) {

            }

            @Override
            public void onLongClick(Tender Tender) {

            }

            @Override
            public void onEditWinnerClick(Tender tender) {

            }
            @Override
            public void onEditStatusClick(Tender tender) {

            }
            @Override
            public void onViewContentEmpClick(Tender tender) {
                Intent emp_view_content = new Intent(TenderActivity.this, ViewContentActivity.class);
                emp_view_content.putExtra("content_emp", tender.getContent());
                startActivity(emp_view_content);
            }
            @Override
            public void onViewContentProClick(Tender tender) {
                Intent pro_view_content = new Intent(TenderActivity.this, ViewContentActivity.class);
                pro_view_content.putExtra("content_pro", tender.getContent());
                startActivity(pro_view_content);

            }

            @Override
            public boolean showProviderLayout(Tender tender) {
                return (!user.isEmployee());
            }

            @Override
            public boolean showEmployeeLayout(Tender tender) {
                return user.isEmployee();
            }
        });
        rvList.setAdapter(tenderAdapter);

        if (isEmployee) {
            BtnCreateTender.setVisibility(View.VISIBLE);
            BtnCreateTender.setOnClickListener(v -> {
                Intent list_to_create_tender = new Intent(TenderActivity.this, CreateTenderActivity.class);
                startActivity(list_to_create_tender);
            });
        }
        else {
            BtnCreateTender.setVisibility(View.GONE);  // hides it fully
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        databaseService.getTenderList(new DatabaseService.DatabaseCallback<List<Tender>>() {
            @Override
            public void onCompleted(List<Tender> tenders) {
                Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", "tenders:" + tenders.size());
                tenderAdapter.setTenderList(tenders);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}