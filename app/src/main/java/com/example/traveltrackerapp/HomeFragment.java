package com.example.traveltrackerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.traveltrackerapp.entities.Place;
import com.example.traveltrackerapp.view_models.PlacesViewModel;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private MapView mapView;
    private PlacesViewModel placesViewModel;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceView) {
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mapView = view.findViewById(R.id.map);

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        placesViewModel.getAllPlaces().observe(getViewLifecycleOwner(), places -> {
            mapView.getOverlays().clear();
            if (places != null && !places.isEmpty()) {
                Place first = places.get(0);
                mapView.getController().setZoom(4.0);
                mapView.getController().setCenter(new GeoPoint(first.getLatitude(), first.getLongitude()));

                for (Place place : places) {
                    Marker marker = new Marker(mapView);
                    marker.setPosition(new GeoPoint(place.getLatitude(), place.getLongitude()));
                    marker.setTitle(place.getName());
                    mapView.getOverlays().add(marker);
                }
            }
            mapView.invalidate();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
    }
}
