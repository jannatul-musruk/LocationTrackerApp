package com.example.locationtrackerapp;

import android.app.Notification;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import androidx.core.app.ActivityCompat;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.example.locationtrackerapp.database.AppDatabase;
import com.example.locationtrackerapp.database.LocationEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationService extends Service {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "LocationService started", Toast.LENGTH_LONG).show();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();
        startLocationUpdates();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                Location location = locationResult.getLastLocation();

                Toast.makeText(getApplicationContext(),
                        "Lat: " + location.getLatitude() +
                                " Lng: " + location.getLongitude(),
                        Toast.LENGTH_LONG).show();

                // Save to database
                executorService.execute(() -> {
                    db.locationDao().insert(new LocationEntity(
                            location.getLatitude(),
                            location.getLongitude(),
                            System.currentTimeMillis()
                    ));
                });
            }
        };

        startForeground(1, createNotification());
        startLocationUpdates();
    }


    private void startLocationUpdates() {

        // 🔐 Permission check
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            stopSelf();
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5 * 60 * 1000);
        locationRequest.setFastestInterval(5 * 60 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;

                Location location = locationResult.getLastLocation();
                if (location == null) return;

                double lat = location.getLatitude();
                double lng = location.getLongitude();

                Toast.makeText(
                        getApplicationContext(),
                        "Lat: " + lat + " Lng: " + lng,
                        Toast.LENGTH_SHORT
                ).show();

                executorService.execute(() ->
                        db.locationDao().insert(
                                new LocationEntity(
                                        lat,
                                        lng,
                                        System.currentTimeMillis()
                                )
                        )
                );
            }
        };

        try {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
            );
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private Notification createNotification() {

        NotificationChannel channel = new NotificationChannel(
                "location_channel",
                "Location Service",
                NotificationManager.IMPORTANCE_LOW
        );

        NotificationManager manager =
                getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        return new NotificationCompat.Builder(this, "location_channel")
                .setContentTitle("Location Service Running")
                .setContentText("Tracking location every 5 minutes")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        executorService.shutdown();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
