package com.example.traveltrackerapp.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.traveltrackerapp.database.AppDatabase;
import com.example.traveltrackerapp.database.PlaceDao;
import com.example.traveltrackerapp.entities.Place;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlacesViewModel extends AndroidViewModel {
    private final PlaceDao dao;
    private final LiveData<List<Place>> allPlaces;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public PlacesViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).placeDao();
        allPlaces = dao.getAllPlaces();
    }

    public LiveData<List<Place>> getAllPlaces() {
        return allPlaces;
    }

    public LiveData<Place> getPlaceById(int id) {
        return dao.getPlaceById(id);
    }

    public void updatePlace(Place place) {
        executorService.execute(() -> dao.updatePlace(place));
    }
}
