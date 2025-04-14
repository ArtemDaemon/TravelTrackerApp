package com.example.traveltrackerapp.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveltrackerapp.R;
import com.example.traveltrackerapp.entities.Place;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    private List<Place> placeList = new ArrayList<>();
    private OnPlaceClickListener listener;

    public void setPlaceList(List<Place> places) {
        this.placeList = places;
        notifyDataSetChanged();
    }

    public interface OnPlaceClickListener {
        void onPlaceClick(Place place);
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, address;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.place_image);
            name = itemView.findViewById(R.id.place_name);
            address = itemView.findViewById(R.id.place_address);
        }
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_place, parent, false);
        return new PlaceViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.name.setText(place.name);
        holder.address.setText(place.address);
        holder.image.setImageURI(Uri.parse(place.imageUri));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlaceClick(place);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public void setOnPlaceClickListener(OnPlaceClickListener listener) {
        this.listener = listener;
    }
}
