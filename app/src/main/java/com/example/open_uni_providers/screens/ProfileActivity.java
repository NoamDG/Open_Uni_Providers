package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.ImageUtil;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;

public class ProfileActivity extends AppCompatActivity {
    TextView FName, LName, ID, Email, Password, Status;
    ImageView PFP;
    User user;
    Button Edit;
    ImageButton Back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = SharedPreferencesUtil.getUser(ProfileActivity.this);
        FName = findViewById(R.id.ProfFN);
        LName = findViewById(R.id.ProfLN);
        ID = findViewById(R.id.ProfID);
        Email = findViewById(R.id.ProfEm);
        Password = findViewById(R.id.ProfPass);
        Status = findViewById(R.id.ProfStatus);
        Back = findViewById(R.id.btn_user_profile_back);
        Edit = findViewById(R.id.btn_edit_user);
        PFP = findViewById(R.id.ProfIm);
        if(user.getIm64()!=null){
            PFP.setImageBitmap(ImageUtil.fromBase64(user.getIm64()));
        }
        else{
            PFP.setVisibility(View.GONE);
        }
        FName.setText(user.getFirstname());
        LName.setText(user.getLastname());
        ID.setText(user.getId());
        Email.setText(user.getEmail());
        Password.setText(user.getPassword());
        if (user.isEmployee()){
            Status.setText("Employee");
        }
        else{
            Status.setText("Provider");
        }
        Back.setOnClickListener(v -> {
            finish();
        });
        Edit.setOnClickListener(v -> {
            Intent edit = new Intent(ProfileActivity.this, UpdateInfoActivity.class);
            startActivity(edit);
        });
    }
}