package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import java.util.function.Predicate;

public class TenderActivity extends AppCompatActivity {
    User user;
    Button BtnCreateTender;
    ImageButton BtnBack;
    boolean isEmployee=false;
    RecyclerView rvList;
    DatabaseService databaseService;
    TenderAdapter tenderAdapter;
    EditText etSearch;
    List<Tender> allTenders = new ArrayList<>();
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
        if(user != null){
            isEmployee = user.isEmployee();
        }
        else{
            isEmployee = false;
        }
        etSearch = findViewById(R.id.et_tender_search);
        BtnCreateTender = findViewById(R.id.btn_create_tender);
        BtnBack = findViewById(R.id.btn_tender_list_back);
        rvList = findViewById(R.id.rv_tender_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));

        BtnBack.setOnClickListener(v -> {
            finish();
        });

        tenderAdapter = new TenderAdapter(new TenderAdapter.OnTenderClickListener() {
            @Override
            public void onClick(Tender tender) {
                Intent viewContent = new Intent(TenderActivity.this, ViewContentActivity.class);
                viewContent.putExtra("subject" ,tender.getTenSubj());
                viewContent.putExtra("status" ,tender.getTenStat());
                viewContent.putExtra("publish" ,tender.getPubDate());
                viewContent.putExtra("expire" ,tender.getExpDate());
                viewContent.putExtra("winner" ,tender.getTenWinner());
                viewContent.putExtra("category" ,tender.getCategory());
                viewContent.putExtra("content", tender.getContent());
                viewContent.putExtra("id", tender.getId());
                startActivity(viewContent);

            }

            @Override
            public void onLongClick(Tender tender) {

            }

            public String StatusLayout(Tender tender) {
                if(user != null){
                    if(user.isEmployee()){
                        return "Employee";
                    }
                    else{
                        return "Provider";
                    }
                }
                return "Guest";
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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTenders(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        databaseService.getTenderList(new DatabaseService.DatabaseCallback<List<Tender>>() {
            @Override
            public void onCompleted(List<Tender> allTenders) {
                Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", "tenders:" + allTenders.size());
                TenderActivity.this.allTenders.clear();
                TenderActivity.this.allTenders.addAll(allTenders);
                filterTenders(etSearch.getText().toString()+"");
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    private void filterTenders(final String text) {
        List<Tender> filterTenders = new ArrayList<>(allTenders);
        if (text.isEmpty()){
            tenderAdapter.setTenderList(allTenders);
            return;
        }

        // remove all tenders from the filter list that DON'T start with text (lower case)
        filterTenders.removeIf(new Predicate<Tender>() {
            @Override
            public boolean test(Tender tender) {
                String tenderCategory = tender.getCategory();
                // if there is no category, remove the tender (failed check)
                if (tenderCategory == null) return true;
                return !tenderCategory.toLowerCase().startsWith(text.toLowerCase());
            }
        });

        tenderAdapter.setTenderList(filterTenders);
    }
}