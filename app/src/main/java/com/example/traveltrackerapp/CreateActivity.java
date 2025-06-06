package com.example.traveltrackerapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.traveltrackerapp.database.AppDatabase;
import com.example.traveltrackerapp.entities.Place;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CreateActivity extends AppCompatActivity {
    private EditText editName, editDescription, editLatitude, editLongitude;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private String selectedImageUri = "android.resource://com.example.traveltrackerapp/"
            + R.drawable.no_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        editName = findViewById(R.id.place_create_name);
        editDescription = findViewById(R.id.place_create_description);
        editLatitude = findViewById(R.id.place_create_latitude);
        editLongitude = findViewById(R.id.place_create_longitude);

        boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            String name = getIntent().getStringExtra("name");
            String description = getIntent().getStringExtra("description");
            double latitude = getIntent().getDoubleExtra("latitude", 0.0);
            double longitude = getIntent().getDoubleExtra("longitude", 0.0);
            String imageUri = getIntent().getStringExtra("imageUri");

            editName.setText(name);
            editDescription.setText(description);
            editLatitude.setText(String.valueOf(latitude));
            editLongitude.setText(String.valueOf(longitude));

            selectedImageUri = imageUri;
            if (imageUri != null && imageUri.startsWith("android.resource://")) {
                ((ImageView) findViewById(R.id.place_create_image)).setImageResource(R.drawable.no_image);
            } else {
                ((ImageView) findViewById(R.id.place_create_image)).setImageURI(Uri.parse(imageUri));
            }
        } else {
            ((ImageView) findViewById(R.id.place_create_image)).setImageResource(R.drawable.no_image);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {finish();});

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        Uri copiedUri = copyImageToInternalStorage(imageUri);

                        if (copiedUri != null) {
                            ImageView imageView = findViewById(R.id.place_create_image);
                            imageView.setImageURI(copiedUri);
                            selectedImageUri = copiedUri.toString();
                        }
                    }
                }
        );

        ImageView imageView = findViewById(R.id.place_create_image);
        imageView.setOnClickListener(v -> openGallery());

        /* Код для помещения картинки в галерею */
//        File file = new File(Environment.getExternalStorageDirectory() + "/Download/place_photo.jpg");
//
//        MediaScannerConnection.scanFile(
//                this,
//                new String[]{file.getAbsolutePath()},
//                null,
//                (path, uri) -> Log.d("MediaScan", "Scanned " + path + ", uri: " + uri));

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_create, menu);
        return true;
    }

    private Uri copyImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File imageFile = new File(getFilesDir(), "place_" + System.currentTimeMillis()
                    + ".jpg");
            OutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            return Uri.fromFile(imageFile); // или лучше использовать FileProvider для Android 7+
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            savePlaceToDatabase();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePlaceToDatabase() {
        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String latStr = editLatitude.getText().toString().trim();
        String lonStr = editLongitude.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || latStr.isEmpty() || lonStr.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude, longitude;
        try {
            latitude = Double.parseDouble(latStr);
            longitude = Double.parseDouble(lonStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Введите корректные координаты", Toast.LENGTH_SHORT).show();
            return;
        }

        if (getIntent().getBooleanExtra("isEdit", false)) {
            int placeId = getIntent().getIntExtra("placeId", -1);
            if (placeId != -1) {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                new Thread(() -> {
                    Place updatedPlace = new Place(name, latitude, longitude, selectedImageUri, description);
                    updatedPlace.setId(placeId);
                    db.placeDao().updatePlace(updatedPlace);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Место обновлено", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }).start();
                return;
            }
        }

        Place newPlace = new Place(name, latitude, longitude, selectedImageUri, description);
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        new Thread(() -> {
            db.placeDao().insert(newPlace);
            runOnUiThread(() -> {
                Toast.makeText(this, "Место сохранено", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
