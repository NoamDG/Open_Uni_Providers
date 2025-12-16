package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.Tender;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.Validator;

public class EditStatusActivity extends AppCompatActivity {
    EditText status;
    Button submit, back;
    DatabaseService databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_status);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseService = DatabaseService.getInstance();
        status = findViewById(R.id.update_status);
        submit = findViewById(R.id.btn_submit_status_update);
        String cstatus = getIntent().getStringExtra("status");
        status.setText(cstatus);
        back = findViewById(R.id.btn_back_from_status_to_tender);
        back.setOnClickListener(v -> {
            Intent back = new Intent(EditStatusActivity.this, TenderActivity.class);
            startActivity(back);
        });
        String id = getIntent().getStringExtra("id");
        submit.setOnClickListener(v -> {
            if(!checkInputStatus(status.getText().toString())) {
                return;
            }
            databaseService.getTender(id, new DatabaseService.DatabaseCallback<Tender>() {
                @Override
                public void onCompleted(Tender tender) {
                    tender.setTenStat(status.getText().toString().trim()+"");
                    databaseService.setTender(tender, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void object) {

                        }

                        @Override
                        public void onFailed(Exception e) {

                        }
                    });
                    Intent from_status_to_tender = new Intent(EditStatusActivity.this, TenderActivity.class);
                    startActivity(from_status_to_tender);
                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        });

    }
    private boolean checkInputStatus(String Status){
        if (!Validator.isStatusValid(Status)) {
            /// show error message to user
            status.setError("Status must be either: Active, Inactive, or Ended");
            /// set focus to status field
            status.requestFocus();
            return false;
        }
        return true;
    }

}