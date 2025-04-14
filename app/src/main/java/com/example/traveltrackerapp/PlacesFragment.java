package com.example.traveltrackerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveltrackerapp.adapters.PlacesAdapter;
import com.example.traveltrackerapp.view_models.PlacesViewModel;

public class PlacesFragment extends Fragment {
    private PlacesAdapter adapter;

    public PlacesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceView) {
        View root = inflater.inflate(R.layout.fragment_places, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PlacesAdapter(requireContext());
        recyclerView.setAdapter(adapter);

        PlacesViewModel viewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        viewModel.getAllPlaces().observe(getViewLifecycleOwner(), places -> {
            adapter.setPlaceList(places);
        });

        return root;
    }
}
