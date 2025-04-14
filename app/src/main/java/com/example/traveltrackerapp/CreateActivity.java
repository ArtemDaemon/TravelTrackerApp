package com.example.traveltrackerapp;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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
    private EditText editName, editDescription, editAddress;
    private Button buttonSave;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private String selectedImageUri = "android.resource://com.example.traveltrackerapp/"
            + R.drawable.no_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

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
        EditText nameEditText = findViewById(R.id.place_create_name);
        EditText descriptionEditText = findViewById(R.id.place_create_description);
        EditText addressEditText = findViewById(R.id.place_create_address);

        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Place newPlace = new Place(name, address, selectedImageUri, description);
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
