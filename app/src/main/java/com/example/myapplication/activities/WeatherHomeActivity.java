package com.example.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.WeatherAdapter;
import com.example.myapplication.adapters.OnItemClickListener;
import com.example.myapplication.objects.CityNameExtractor;
import com.example.myapplication.objects.Weather;
import com.example.myapplication.parsers.WeatherXmlPullParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherHomeActivity extends AppCompatActivity implements OnItemClickListener {

    private List<RecyclerView> recyclerViews = new ArrayList<>();
    private Map<String, String> matchedCities = new HashMap<>();
    private String cityName;
    private Context context;
    private List<List<Weather>> weatherLists = new ArrayList<>();
    private int recyclerViewCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_home);
        Log.d("WeatherHomeActivity", "onCreate called");

        LinearLayout layout = findViewById(R.id.weatherContainer);

        for (int i = 0; i < MainActivity.apiUrls.length; i++) {
            RecyclerView recyclerView = new RecyclerView(this);
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            recyclerView.setId(i + 1);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            layout.addView(recyclerView);
            recyclerViews.add(recyclerView);

            fetchWeatherData(MainActivity.apiUrls[i], recyclerView);
        }
    }

    private void fetchWeatherData(String apiUrl, RecyclerView recyclerView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        connection.disconnect();

                        WeatherXmlPullParser parser = new WeatherXmlPullParser();
                        List<Weather> weatherList = parser.parse(response.toString());
                        String locationId = getlocationId(apiUrl);
                        context = getApplicationContext();
                        cityName = CityNameExtractor.extractCityName(context, locationId);
                        matchedCities.put(locationId, cityName);
                        Log.d("WeatherHomeActivity", "Matched Cities" + matchedCities);
                        Log.d("WeatherHomeActivity", "Location ID: " + locationId);
                        Log.d("WeatherHomeActivity", "City Name: " + cityName);

                        // Set location ID for each weather object
                        for (Weather weather : weatherList) {
                            weather.setLocationId(locationId);
                        }

                        // Post UI updates back to the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                weatherLists.add(weatherList);
                                recyclerViewCount++;
                                if (recyclerViewCount == MainActivity.apiUrls.length) {
                                    createAndSetAdapters();
                                }
                            }
                        });
                    } else {
                        Log.e("WeatherHomeActivity", "Failed to fetch data from API. Response code: " + responseCode);
                    }
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    private void createAndSetAdapters() {
        for (int i = 0; i < recyclerViews.size(); i++) {
            List<Weather> weatherList = weatherLists.get(i);
            RecyclerView recyclerView = recyclerViews.get(i);
            WeatherAdapter adapter = new WeatherAdapter(context,weatherList, matchedCities);
            adapter.setOnItemClickListener(WeatherHomeActivity.this);
            recyclerView.setAdapter(adapter);
        }
    }

    private String getlocationId(String url) {
        String[] parts = url.split("/");
        String locationId = parts[parts.length - 1].replaceAll("[^\\d.]", "");
        Log.d("WeatherHomeActivity", "Location ID from URL: " + locationId);
        return locationId;
    }

    @Override
    public void onItemClick(Weather weather) {
        Intent intent = new Intent(WeatherHomeActivity.this, WeatherFieldActivity.class);
        intent.putExtra("cityName", matchedCities.get(weather.getlocationId())); // Pass the city name through intent
        intent.putExtra("weather", weather);
        startActivity(intent);
    }
}
