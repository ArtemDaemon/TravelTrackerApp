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

    @NonNull
    private String address;

    @NonNull
    private String imageUri;

    @NonNull
    private String description;

    public Place(@NonNull String name, @NonNull String address, @NonNull String imageUri,
                 @NonNull String description) {
        this.name = name;
        this.address = address;
        this.imageUri = imageUri;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
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

    public void setAddress(@NonNull String address) {
        this.address = address;
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
