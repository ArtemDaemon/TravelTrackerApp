package com.example.traveltrackerapp.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.traveltrackerapp.database.AppDatabase;
import com.example.traveltrackerapp.database.PlaceDao;
import com.example.traveltrackerapp.entities.Place;

import java.util.List;

public class PlacesViewModel extends AndroidViewModel {
    private LiveData<List<Place>> allPlaces;

    public PlacesViewModel(@NonNull Application application) {
        super(application);
        PlaceDao dao = AppDatabase.getInstance(application).placeDao();
        allPlaces = dao.getAllPlaces();
    }

    public LiveData<List<Place>> getAllPlaces() {
        return allPlaces;
    }
}
