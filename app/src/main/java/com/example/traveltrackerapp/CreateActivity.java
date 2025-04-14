package com.example.traveltrackerapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CreateActivity extends AppCompatActivity {
    private EditText editName, editDescription, editAddress;
    private Button buttonSave;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private String selectedImageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {finish();});

        EditText nameEditText = findViewById(R.id.place_create_name);
        EditText descriptionEditText = findViewById(R.id.place_create_description);
        EditText addressEditText = findViewById(R.id.place_create_address);

        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        ImageView imageView = findViewById(R.id.place_create_image);
                        imageView.setImageURI(imageUri);
                        selectedImageUri = imageUri.toString();
                    }
                }
        );

        ImageView imageView = findViewById(R.id.place_create_image);
        imageView.setOnClickListener(v -> openGallery());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
}
