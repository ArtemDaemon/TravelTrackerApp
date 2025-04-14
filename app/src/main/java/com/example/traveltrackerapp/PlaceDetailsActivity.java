package com.example.traveltrackerapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.traveltrackerapp.entities.Place;
import com.example.traveltrackerapp.view_models.PlacesViewModel;

public class PlaceDetailsActivity extends AppCompatActivity {
    private Place currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

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
                ((TextView) findViewById(R.id.place_detail_address)).setText(place.getAddress());
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {finish();});
    }
}
