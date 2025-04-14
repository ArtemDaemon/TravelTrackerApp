package com.example.traveltrackerapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.traveltrackerapp.entities.Place;
import com.example.traveltrackerapp.view_models.PlacesViewModel;

public class PlaceDetailsActivity extends AppCompatActivity {
    private PlacesViewModel viewModel;
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

        viewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        viewModel.getPlaceById(placeId).observe(this, place -> {
            if (place != null) {
                currentPlace = place;
            }
        });
    }
}
