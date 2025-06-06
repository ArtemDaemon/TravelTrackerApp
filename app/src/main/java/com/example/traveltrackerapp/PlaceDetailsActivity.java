package com.example.traveltrackerapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.traveltrackerapp.database.AppDatabase;
import com.example.traveltrackerapp.entities.Place;
import com.example.traveltrackerapp.view_models.PlacesViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlaceDetailsActivity extends AppCompatActivity {
    private Place currentPlace;
    private TextView weatherInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        weatherInfoTextView = findViewById(R.id.weather_info);

        int placeId = getIntent().getIntExtra("place_id", 1);
        if (placeId == -1) {
            Toast.makeText(this, "Invalid place ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        PlacesViewModel viewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        viewModel.getPlaceById(placeId).observe(this, place -> {
            if (place != null) {
                currentPlace = place;
                ((ImageView) findViewById(R.id.place_detail_image)).
                        setImageURI(Uri.parse(place.getImageUri()));
                ((TextView) findViewById(R.id.place_detail_name)).setText(place.getName());
                ((TextView) findViewById(R.id.place_detail_description))
                        .setText(place.getDescription());
                ((TextView) findViewById(R.id.place_detail_latlng))
                        .setText("Координаты: " + place.getLatitude() + ", " + place.getLongitude());
                fetchWeather(place.getLatitude(), place.getLongitude());
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {finish();});
    }

    private void fetchWeather(double lat, double lon) {
        new Thread(() -> {
            try {
                String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + lat +
                        "&longitude=" + lon +
                        "&current_weather=true&timezone=auto";
                URL url = new URL(urlString);
                String info = getString(url);
                new Handler(Looper.getMainLooper()).post(() -> weatherInfoTextView.setText(info));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        weatherInfoTextView.setText("Не удалось загрузить погоду"));
            }
        }).start();
    }

    @NonNull
    private static String getString(URL url) throws IOException, JSONException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        reader.close();
        JSONObject json = new JSONObject(sb.toString());
        JSONObject currentWeather = json.getJSONObject("current_weather");
        double temp = currentWeather.getDouble("temperature");
        double wind = currentWeather.getDouble("windspeed");
        return "Погода: " + temp + "°C, ветер " + wind + " км/ч";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_edit) {
            Intent intent = new Intent(this, CreateActivity.class);
            intent.putExtra("isEdit", true);
            intent.putExtra("placeId", currentPlace.getId());
            intent.putExtra("name", currentPlace.getName());
            intent.putExtra("description", currentPlace.getDescription());
            intent.putExtra("latitude", currentPlace.getLatitude());
            intent.putExtra("longitude", currentPlace.getLongitude());
            intent.putExtra("imageUri", currentPlace.getImageUri());
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.action_cart) {
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Удалить место")
                .setMessage("Вы уверены, что хотите удалить это место?")
                .setPositiveButton("Удалить",
                        (dialog, which) -> deletePlace())
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void deletePlace() {
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        new Thread(() -> {
            db.placeDao().delete(currentPlace);
            runOnUiThread(() -> {
                Toast.makeText(this, "Место удалено", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
