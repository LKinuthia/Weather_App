package com.example.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.WeatherAdapter;
import com.example.myapplication.objects.CityNameExtractor;
import com.example.myapplication.objects.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

     //Array of API URLs
    public static final String[] apiUrls = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/1185241"
    };

    public static final String[] threeDayapiUrls = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("MainActivity", "onCreate called");

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (isNetworkAvailable()) {
            startProgress();
        } else {
            Toast.makeText(this, "No internet connection. Please turn on data or Wi-Fi.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void startProgress() {
        // Start WeatherHomeActivity
        startActivity(WeatherHomeActivity.class);
    }

    private void startActivity(Class<?> cls) {
        // Start a new activity
        startActivity(new Intent(this, cls));
    }

}









//package com.example.myapplication.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.myapplication.R;
//import com.example.myapplication.adapters.WeatherAdapter;
//import com.example.myapplication.objects.Weather;
//import com.example.myapplication.parsers.WeatherXmlPullParser;
//
//import org.xmlpull.v1.XmlPullParserException;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//
//    // Array of API URLs
//    private final String[] apiUrls = {
//            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643743",
//            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2648579",
//            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/5128581",
//            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/287286",
//            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/934154",
//            "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/1185241"
//    };
//
//    private WeatherAdapter adapter;
//    public static List<Weather> weatherList = new ArrayList<>();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Log.d("MainActivity", "onCreate called");
//
//        // Set up the start button click listener
//        Button startButton = findViewById(R.id.startButton);
//        startButton.setOnClickListener(this);
//
//        // Initialize RecyclerView and adapter
//        //RecyclerView recyclerView = findViewById(R.id.view1);
//        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        //adapter = new WeatherAdapter(weatherList);
//        //recyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    public void onClick(View view) {
//        startProgress();
//    }
//
//    private void startProgress() {
//        // Iterate through the array of API URLs
//        for (String apiUrl : apiUrls) {
//            Thread thread = new Thread(new Task(apiUrl));
//            thread.start();
//        }
//    }
//
//    // Task class for fetching data without AsyncTask
//    private class Task implements Runnable {
//        private String url;
//
//        public Task(String aurl) {
//            url = aurl;
//        }
//
//        @Override
//        public void run() {
//            String result = "";
//            URL aurl;
//            URLConnection yc;
//            BufferedReader in = null;
//            String inputLine = "";
//
//            Log.e("MyTag", "in run");
//
//            try {
//                Log.e("MyTag", "in try");
//                aurl = new URL(url);
//                yc = aurl.openConnection();
//                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//                while ((inputLine = in.readLine()) != null) {
//                    result = result + inputLine;
//                    Log.e("MyTag", inputLine);
//                }
//                in.close();
//            } catch (IOException ae) {
//                Log.e("MyTag", "ioexception");
//            }
//
//            // Parse the XML data using the XmlPullParser
//            WeatherXmlPullParser parser = new WeatherXmlPullParser();
//            List<Weather> weatherData = null;
//            try {
//                weatherData = parser.parse(result);
//            } catch (XmlPullParserException | IOException e) {
//                e.printStackTrace();
//            }
//
//            // Update UI with the fetched weather data
//            if (weatherData != null) {
//                // Process the weather data and update the adapter
//                //processWeatherData(weatherData);
//                updateUI(weatherData);
//            } else {
//                Log.e("MainActivity", "Failed to fetch weather data or result is empty.");
//            }
//        }
//    }
//
//    private void updateUI(List<Weather> weatherData) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                // Clear previous data
//                weatherList.clear();
//                // Add new weather data
//                weatherList.addAll(weatherData);
//                // Notify adapter
//                //adapter.notifyDataSetChanged();
//                RecyclerView recyclerView = findViewById(R.id.view1);
//                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//                adapter = new WeatherAdapter(weatherList);
//                recyclerView.setAdapter(adapter);
//            }
//        });
//    }
//}
