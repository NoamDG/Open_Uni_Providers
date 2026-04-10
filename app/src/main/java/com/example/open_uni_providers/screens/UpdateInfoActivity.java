package com.example.open_uni_providers.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.adapters.ImageSourceAdapter;
import com.example.open_uni_providers.models.ImageSourceOption;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.ImageUtil;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;
import com.example.open_uni_providers.utils.Validator;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class UpdateInfoActivity extends AppCompatActivity {
    EditText Pass, Em;
    Button Submit;
    ImageButton BtnBack;
    User user;
    String id;
    ImageView PFP;
    DatabaseService databaseService;
    private ActivityResultLauncher<Intent> selectImageLauncher;
    private ActivityResultLauncher<Intent> captureImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = SharedPreferencesUtil.getUser(UpdateInfoActivity.this);
        Pass = findViewById(R.id.etPassword);
        PFP = findViewById(R.id.current_pfp);
        if (user.getIm64() != null) {
            PFP.setImageBitmap(ImageUtil.fromBase64(user.getIm64()));
        } else {
            PFP.setVisibility(View.GONE);
        }
        Em = findViewById(R.id.etEmail);
        BtnBack = findViewById(R.id.btn_update_back);
        user = SharedPreferencesUtil.getUser(UpdateInfoActivity.this);
        databaseService = DatabaseService.getInstance();
        BtnBack.setOnClickListener(v -> {
            finish();
        });
        Submit = findViewById(R.id.btn_submit_update_info);
        Pass.setText(user.getPassword());
        Em.setText(user.getEmail());
        id = user.getId();
        PFP.setOnClickListener(v -> {
            showImageSourceDialog();
            return;
        });

        /// register the activity result launcher for selecting image from gallery
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        PFP.setImageURI(selectedImage);
                        /// set the tag for the image view to null
                        PFP.setTag(null);
                    }
                });

        /// register the activity result launcher for capturing image from camera
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        PFP.setImageBitmap(bitmap);
                        PFP.setTag(null);
                    }
                });
        Submit.setOnClickListener(v -> {
            if (!checkInputUpdate(Pass.getText().toString(), Em.getText().toString())) {
                return;
            }
            String imageBase64 = ImageUtil.toBase64(PFP);
            databaseService.getUser(id, new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    user.setEmail(Em.getText().toString().trim() + "");
                    user.setPassword(Pass.getText().toString().trim() + "");
                    user.setIm64(imageBase64);
                    databaseService.setUser(user, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void object) {

                        }

                        @Override
                        public void onFailed(Exception e) {

                        }
                    });
                    Intent from_update_to_main = new Intent(UpdateInfoActivity.this, MainActivity.class);
                    startActivity(from_update_to_main);
                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        });
    }

    private void showImageSourceDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_image_source, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        final ArrayList<ImageSourceOption> options = new ArrayList<>();
        options.add(new ImageSourceOption("Gallery", "Select image from the gallery", R.drawable.image_gallery));
        options.add(new ImageSourceOption("Camera", "Take a picture", R.drawable.image_camera));

        ListView listView = bottomSheetView.findViewById(R.id.list_view_image_sources);
        ImageSourceAdapter adapter = new ImageSourceAdapter(this, options, option -> {
            bottomSheetDialog.dismiss();
            if (option.getTitle().equals("Gallery")) {
                selectImageFromGallery();
            } else if (option.getTitle().equals("Camera")) {
                captureImageFromCamera();
            }
        });
        listView.setAdapter(adapter);

        bottomSheetDialog.show();
    }

    /// select image from gallery
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    /// capture image from camera
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }

    private boolean checkInputUpdate(String password, String email) {
        if (!Validator.isEmailValid(email)) {
            Em.setError("Invalid email address");
            /// set focus to email field
            Em.requestFocus();
            return false;
        }

        if (!Validator.isPasswordValid(password)) {
            Pass.setError("Password must be at least 6 characters long");
            /// set focus to password field
            Pass.requestFocus();
            return false;
        }
        return true;
    }
}