package com.example.traveltrackerapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.traveltrackerapp.R;
import com.example.traveltrackerapp.entities.Place;

import java.util.concurrent.Executors;

@Database(entities = {Place.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract PlaceDao placeDao();

    private static Context appContext;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            appContext = context.getApplicationContext();
            instance = Room.databaseBuilder(appContext,
                    AppDatabase.class, "places_db")
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase database = getInstance(appContext);
                database.placeDao().insert(new Place(
                        "Тестовое название",
                        "Тестовый адрес",
                        "android.resource://com.example.traveltrackerapp/"
                                + R.drawable.no_image,
                        "Тестовое описание"
                ));
            });
        }
    };
}
