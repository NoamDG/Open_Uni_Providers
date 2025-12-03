package com.example.open_uni_providers.screens;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
    Button BtnLogout, btn_tender_red, btn_exempt_red, btn_bidder_list_red, btn_general_terms_red, btn_contact_red;
    String FName, LName;
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
        Log.d(TAG, "User: " + user);
        Name = findViewById(R.id.NameView);
        FName = user.getFirstname();
        LName = user.getLastname();
        Name.setText(FName+" "+LName);
        BtnLogout = findViewById(R.id.btn_main_logout);
        BtnLogout.setOnClickListener(v -> {
            // Clear saved user
            SharedPreferencesUtil.signOutUser(MainActivity.this);

            // Go back to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btn_tender_red = findViewById(R.id.btn_tender_red);
        btn_tender_red.setOnClickListener(v -> {
            Intent intentTender = new Intent(MainActivity.this, TenderActivity.class);
            startActivity(intentTender);
        });
        btn_exempt_red = findViewById(R.id.btn_exempt_red);
        btn_exempt_red.setOnClickListener(v -> {
            Intent intentExempt = new Intent(MainActivity.this, TenderExemptActivity.class);
            startActivity(intentExempt);
        });
        btn_bidder_list_red = findViewById(R.id.btn_bidder_list_red);
        btn_bidder_list_red.setOnClickListener(v -> {
            Intent intentBidder = new Intent(MainActivity.this, BidderListActivity.class);
            startActivity(intentBidder);
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