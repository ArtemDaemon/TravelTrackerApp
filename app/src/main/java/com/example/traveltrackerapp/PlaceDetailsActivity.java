package com.example.traveltrackerapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.traveltrackerapp.database.AppDatabase;
import com.example.traveltrackerapp.entities.Place;

public class PlaceDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        int placeId = getIntent().getIntExtra("place_id", -1);

        AppDatabase db = AppDatabase.getInstance(this);
        Place place = db.placeDao().getPlaceById(placeId);

        if (place != null) {

        }

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
