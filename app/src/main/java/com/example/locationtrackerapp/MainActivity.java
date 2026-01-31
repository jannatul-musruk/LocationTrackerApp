package com.example.locationtrackerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btnStartLocation, btnStopLocation, btnFetchAPI;
    private TextView txtApiResult;

    private static final int REQ_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartLocation = findViewById(R.id.btnStartLocation);
        btnStopLocation = findViewById(R.id.btnStopLocation);
        btnFetchAPI = findViewById(R.id.btnFetchAPI);
        txtApiResult = findViewById(R.id.txtApiResult);

        // Start Location
        btnStartLocation.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                startLocationService();

            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQ_CODE
                );
            }
        });

        // Stop Location
        btnStopLocation.setOnClickListener(v -> {
            stopService(new Intent(this, LocationService.class));
            Toast.makeText(this,
                    "Location Service Stopped",
                    Toast.LENGTH_SHORT).show();
        });

        // Fetch API
        btnFetchAPI.setOnClickListener(v -> fetchApiData());

        // Auto fetch once
        fetchApiData();
    }

    private void startLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startLocationService();
        }
    }

    // ✅ ONE correct method name everywhere
    private void fetchApiData() {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.chucknorris.io/jokes/random");
                HttpURLConnection conn =
                        (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                runOnUiThread(() ->
                        txtApiResult.setText(sb.toString())
                );

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this,
                                "Failed to fetch API",
                                Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}
