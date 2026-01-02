package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.adapters.UserAdapter;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.services.DatabaseService;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    RecyclerView rvList;
    DatabaseService databaseService;
    Button back;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseService = DatabaseService.getInstance();
        back = findViewById(R.id.btn_from_user_list_to_admin);
        back.setOnClickListener(v -> {
            Intent back = new Intent(AdminActivity.this, MainActivity.class);
            startActivity(back);
        });
        rvList = findViewById(R.id.rv_user_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(new UserAdapter.OnUserClickListener() {
            @Override
            public void onClick(User user) {

            }

            @Override
            public void onLongClick(User user) {

            }
            @Override
            public void onMakeUserAdminClick(User user) {
                if(!user.isAdmin() && user.isEmployee()){
                    new android.app.AlertDialog.Builder(AdminActivity.this)
                            .setTitle("Make User Admin")
                            .setMessage("Are you sure you want to make " + user.getFirstname() + " " + user.getLastname() + " admin?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                user.setAdmin(true);
                                databaseService.setUser(user, new DatabaseService.DatabaseCallback<Void>() {
                                    @Override
                                    public void onCompleted(Void object) {
                                        // --- REFRESH CODE GOES HERE ---
                                        // Option A: Just refresh the whole list from DB (Safest)
                                        onResume();

                                        // Option B: If you want to be fancy/fast, just tell the
                                        // adapter this specific item changed:
                                        // userAdapter.notifyDataSetChanged();

                                        Toast.makeText(AdminActivity.this, "User is now Admin", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailed(Exception e) {
                                        Toast.makeText(AdminActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            })
                            .setNegativeButton("Cancel", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                if(!user.isEmployee()){
                    Toast.makeText(AdminActivity.this, "User is not an employee", Toast.LENGTH_SHORT).show();
                }
                if(user.isAdmin()){
                    Toast.makeText(AdminActivity.this, "User is already admin", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onUpdateUserClick(User user) {
                Intent edit_user = new Intent(AdminActivity.this, AdminEditUserActivity.class);
                edit_user.putExtra("firstname", user.getFirstname());
                edit_user.putExtra("lastname", user.getLastname());
                edit_user.putExtra("id", user.getId());
                edit_user.putExtra("email", user.getEmail());
                edit_user.putExtra("password", user.getPassword());
                startActivity(edit_user);
            }
            @Override
            public void onDeleteUserClick(User user) {
                new android.app.AlertDialog.Builder(AdminActivity.this)
                        .setTitle("Delete User")
                        .setMessage("Are you sure you want to delete " + user.getFirstname() + " " + user.getLastname() + "?")
                        .setPositiveButton("Yes, Delete", (dialog, which) -> {

                            // 2. Call your DatabaseService delete method
                            databaseService.deleteUser(user.getId(), new DatabaseService.DatabaseCallback<Void>() {
                                @Override
                                public void onCompleted(Void result) {
                                    // 3. Remove from the RecyclerView list immediately with animation
                                    userAdapter.removeUser(user);

                                    android.widget.Toast.makeText(AdminActivity.this,
                                            "User deleted successfully", android.widget.Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(Exception e) {
                                    // Show error if something went wrong (e.g. no internet)
                                    android.widget.Toast.makeText(AdminActivity.this,
                                            "Failed to delete: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        rvList.setAdapter(userAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();

        databaseService.getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", "users:" + users.size());
                userAdapter.setUserList(users);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}