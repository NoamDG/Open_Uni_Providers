package com.example.open_uni_providers.screens;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity";
    TextView Name;
    User user;
    Button BtnLogout,BtnLogin,BtnRegister, BtnUpdate, btn_tender_red, btn_app_red, btn_general_terms_red, btn_contact_red, btn_admin;
    String FName, LName;
    LinearLayout Guestl, Userl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = SharedPreferencesUtil.getUser(MainActivity.this);
        Guestl = findViewById(R.id.guest_layout);
        Userl = findViewById(R.id.user_layout);
        Name = findViewById(R.id.NameView);
        BtnUpdate = findViewById(R.id.btn_from_main_to_update);
        BtnRegister = findViewById(R.id.btn_from_main_to_register);
        btn_app_red = findViewById(R.id.btn_view_applications);
        BtnLogout = findViewById(R.id.btn_main_logout);
        BtnLogin = findViewById(R.id.btn_main_login);
        btn_admin = findViewById(R.id.btn_admin_page);
        if(user != null){
            FName = user.getFirstname();
            LName = user.getLastname();
            Name.setText(FName+" "+LName);
            Userl.setVisibility(View.VISIBLE);
            Guestl.setVisibility(View.GONE);
            if (user.isAdmin()) {
                btn_admin.setVisibility(View.VISIBLE);
                btn_admin.setOnClickListener(v -> {
                    Intent main_to_admin = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(main_to_admin);
                });
            }
            else {
                btn_admin.setVisibility(View.GONE);  // hides it fully
            }
            BtnLogout.setOnClickListener(v -> {
                // Clear saved user
                SharedPreferencesUtil.signOutUser(MainActivity.this);

                // Go back to LoginActivity
                Intent intent = new Intent(MainActivity.this, LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
            BtnUpdate.setOnClickListener(v -> {
                Intent intentUpdate = new Intent(MainActivity.this, UpdateInfoActivity.class);
                startActivity(intentUpdate);
            });
            if (user.isEmployee()) {
                btn_app_red.setVisibility(View.VISIBLE);
                btn_app_red.setOnClickListener(v -> {
                    Intent intentApp = new Intent(MainActivity.this, ApplyListActivity.class);
                    startActivity(intentApp);
                });
            }
            else {
                btn_app_red.setVisibility(View.GONE);
            }
        }
        else{
            Userl.setVisibility(View.GONE);
            Name.setText("Guest");
            btn_admin.setVisibility(View.GONE);
            Guestl.setVisibility(View.VISIBLE);
            btn_app_red.setVisibility(View.GONE);
            BtnLogin.setOnClickListener(v -> {
                Intent log = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(log);
            });
            BtnRegister.setOnClickListener(v -> {
                Intent reg = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(reg);
            });
        }

        btn_tender_red = findViewById(R.id.btn_tender_red);
        btn_tender_red.setOnClickListener(v -> {
            Intent intentTen = new Intent(MainActivity.this, TenderActivity.class);
            startActivity(intentTen);
        });
        btn_general_terms_red = findViewById(R.id.btn_general_terms_red);
        btn_general_terms_red.setOnClickListener(v -> {
            Intent intentTerms = new Intent(MainActivity.this, GeneralTermsActivity.class);
            startActivity(intentTerms);
        });
        btn_contact_red = findViewById(R.id.btn_contact_red);
        btn_contact_red.setOnClickListener(v -> {
            Intent intentContact = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intentContact);
        });

    }
}