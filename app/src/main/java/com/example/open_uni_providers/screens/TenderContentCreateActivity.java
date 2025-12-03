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
import com.example.open_uni_providers.models.TenderContent;

public class TenderContentCreateActivity extends AppCompatActivity {
    EditText content;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tender_content_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        content = findViewById(R.id.content_text);
        btnSubmit = findViewById(R.id.btn_submit_create_tender_content);
        btnSubmit.setOnClickListener(v -> {

            Intent intent = new Intent(TenderContentCreateActivity.this, CreateTenderActivity.class);
            intent.putExtra("tenderCon", content.getText().toString());
            startActivity(intent);

        });
    }
}