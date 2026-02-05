package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.adapters.ApplicationAdapter;
import com.example.open_uni_providers.adapters.TenderAdapter;
import com.example.open_uni_providers.models.Application;
import com.example.open_uni_providers.models.Tender;
import com.example.open_uni_providers.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ApplyListActivity extends AppCompatActivity {
    RecyclerView rvList;
    DatabaseService databaseService;
    Button back;
    ApplicationAdapter appAdapter;
    List<Application> allApps = new ArrayList<>();
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_apply_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseService = DatabaseService.getInstance();
        back = findViewById(R.id.btn_apply_list_back);
        subject = getIntent().getStringExtra("subject");
        back.setOnClickListener(v -> {
            finish();
        });
        rvList = findViewById(R.id.rv_apply_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));

        appAdapter = new ApplicationAdapter(new ApplicationAdapter.OnApplicationClickListener() {
            @Override
            public void onClick(Application app) {

            }

            @Override
            public void onLongClick(Application app) {

            }
            @Override
            public void onViewContentClick(Application apply) {
                Intent view_content = new Intent(ApplyListActivity.this, ViewApplicationActivity.class);
                view_content.putExtra("content", apply.getContent());
                view_content.putExtra("fname", apply.getfName());
                view_content.putExtra("lname", apply.getlName());
                view_content.putExtra("subject", apply.getSubject());
                startActivity(view_content);
            }
        });
        rvList.setAdapter(appAdapter);

    }
    @Override
    protected void onResume() {
        super.onResume();

        databaseService.getApplicationList(new DatabaseService.DatabaseCallback<List<Application>>() {
            @Override
            public void onCompleted(List<Application> apps) {
                Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", "applications:" + apps.size());
                allApps.clear();
                allApps.addAll(apps);
                filterApps(subject);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
    private void filterApps(final String text) {
        List<Application> filterApps = new ArrayList<>(allApps);
        if (text == null || text.isBlank()){
            appAdapter.setApplicationList(allApps);
            return;
        }

        // remove all tenders from the filter list that DON'T start with text (lower case)
        filterApps.removeIf(new Predicate<Application>() {
            @Override
            public boolean test(Application application) {
                String appSubject = application.getSubject()+"";
                return !appSubject.equals(text + "");
            }
        });

        appAdapter.setApplicationList(filterApps);
    }
}