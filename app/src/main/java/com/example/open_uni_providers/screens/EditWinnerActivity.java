package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class EditWinnerActivity extends AppCompatActivity {
    EditText winner;
    Button submit, back;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_winner);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseService = DatabaseService.getInstance();
        winner = findViewById(R.id.update_winner);
        submit = findViewById(R.id.btn_submit_winner_update);
        back = findViewById(R.id.btn_back_from_winner_to_tender);
        back.setOnClickListener(v -> {
            Intent back = new Intent(EditWinnerActivity.this, TenderActivity.class);
            startActivity(back);
        });
        String cwinner = getIntent().getStringExtra("winner");
        winner.setText(cwinner);
        String id = getIntent().getStringExtra("id");
        submit.setOnClickListener(v -> {
            if(!checkInputStatus(winner.getText().toString())) {
                return;
            }
            databaseService.getTender(id, new DatabaseService.DatabaseCallback<Tender>() {
                @Override
                public void onCompleted(Tender tender) {
                    tender.setTenWinner(winner.getText().toString());
                    databaseService.setTender(tender, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void object) {

                        }

                        @Override
                        public void onFailed(Exception e) {

                        }
                    });
                    if(!winner.getText().toString().equals("")){
                        tender.setTenStat("Inactive");
                        databaseService.setTender(tender, new DatabaseService.DatabaseCallback<Void>() {
                            @Override
                            public void onCompleted(Void object) {

                            }

                            @Override
                            public void onFailed(Exception e) {

                            }
                        });
                    }
                    else{
                        tender.setTenStat("Active");
                        databaseService.setTender(tender, new DatabaseService.DatabaseCallback<Void>() {
                            @Override
                            public void onCompleted(Void object) {

                            }

                            @Override
                            public void onFailed(Exception e) {

                            }
                        });
                    }
                    Intent from_winner_to_tender = new Intent(EditWinnerActivity.this, TenderActivity.class);
                    startActivity(from_winner_to_tender);
                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        });

    }
    private boolean checkInputStatus(String Winner){
        if (!Validator.isWinnerValid(Winner)) {
            winner.setError("Last name must be at least 3 characters long or blank");
            /// set focus to winner name field
            winner.requestFocus();
            return false;
        }
        return true;
    }

}