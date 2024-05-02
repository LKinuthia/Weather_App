// WeatherFieldActivity.java
package com.example.myapplication.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.objects.Weather;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class WeatherFieldActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap googleMap;
   private String cityName;
    private TextView citytextView;
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView windSpeedTextView;
    private TextView pressureTextView;
    private TextView visibilityTextView;
    private TextView pubDateTextView;
    public TextView windDirectionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.weather_field);
        } else {
            setContentView(R.layout.weather_field);
        }

        // Get the city name from the intent
        cityName = getIntent().getStringExtra("cityName");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        // Initialize UI elements
        citytextView = findViewById(R.id.citytextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        pressureTextView = findViewById(R.id.pressureTextView);
        visibilityTextView = findViewById(R.id.visibilityTextView);
        pubDateTextView = findViewById(R.id.pubDateTextView);
        windDirectionTextView = findViewById(R.id.windDirectionTextView);

        Weather weather = (Weather) getIntent().getSerializableExtra("weather");
        if (weather != null) {
            // Update UI elements with weather data
            String cityName = getIntent().getStringExtra("cityName");
            if (cityName != null) {
                citytextView.setText(cityName);
            } else {
                citytextView.setText("Unknown City");
            }
            temperatureTextView.setText(weather.getTemperature());
            humidityTextView.setText(weather.getHumidity());
            windSpeedTextView.setText(weather.getWindSpeed());
            pressureTextView.setText(weather.getPressure());
            visibilityTextView.setText("Visibility: " + weather.getVisibility());
            pubDateTextView.setText(weather.getPubDate());
            windDirectionTextView.setText(weather.getwindDirection());
        } else {
            // Handle case where weather data is missing
            citytextView.setText("Weather data not available");
            temperatureTextView.setText("Weather data not available");
            humidityTextView.setText("Weather data not available");
            windSpeedTextView.setText("Weather data not available");
            pressureTextView.setText("Weather data not available");
            visibilityTextView.setText("Weather data not available");
            pubDateTextView.setText("Weather data not available");
            windDirectionTextView.setText("Weather data not available");
        }

        ImageView nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the FutureForecastActivity
                Intent intent = new Intent(WeatherFieldActivity.this, FutureForecastActivity.class);

                // Pass the city name as an extra to the intent
                String cityName = citytextView.getText().toString();
                intent.putExtra("cityName", cityName);

                // Start the FutureForecastActivity
                startActivity(intent);
            }
        });

    ImageView backButton = findViewById(R.id.backButton);
    backButton.setOnClickListener(new View.OnClickListener()

    {

        @Override
        public void onClick (View v){
        // Create an Intent to navigate to the FutureForecastActivity
        Intent intent = new Intent(WeatherFieldActivity.this, WeatherHomeActivity.class);

        // Start the FutureForecastActivity
        startActivity(intent);
    }
    });
}

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Get the coordinates of the city
        LatLng cityLatLng = getLocationFromAddress(cityName);

        if (cityLatLng != null) {
            // Add marker for the city
            googleMap.addMarker(new MarkerOptions().position(cityLatLng).title(cityName));

            // Move camera to the city location
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLatLng, 10f));
        } else {
            Toast.makeText(this, "Unable to find coordinates for the city", Toast.LENGTH_SHORT).show();
        }
    }

     // to get LatLng from city name using Geocoder
    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng latLng = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}
