package com.example.traveltrackerapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "places")
public class Place {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name;

    private double latitude;
    private double longitude;

    @NonNull
    private String imageUri;

    @NonNull
    private String description;

    public Place(@NonNull String name, double latitude, double longitude, @NonNull String imageUri,
                 @NonNull String description) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUri = imageUri;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setImageUri(@NonNull String imageUri) {
        this.imageUri = imageUri;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }
}
