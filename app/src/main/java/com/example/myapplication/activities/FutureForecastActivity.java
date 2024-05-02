package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.objects.Weather;
import com.example.myapplication.objects.WeatherForecast;

import com.example.myapplication.parsers.WeatherForecastXmlPullParser;
import com.example.myapplication.parsers.WeatherXmlPullParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FutureForecastActivity extends AppCompatActivity {

    private TextView temperatureTextView;
    private TextView temperatureTextView1;
    private TextView humidityTextView;
    private TextView windSpeedTextView;
    private TextView uvRiskTextView;
    private TextView titleTextView;
    public TextView descriptionTextView;
    public TextView day2;
    public TextView minimumTextView;
    public TextView maximumTextView;
    public TextView day3;
    public TextView minTextView;
    public TextView maxTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.futureforecast); // Set content view to futureforecast.xml

        // Initialize UI elements
        temperatureTextView = findViewById(R.id.temperatureTextView);
        temperatureTextView1 = findViewById(R.id.temperatureTextView1);
        humidityTextView = findViewById(R.id.humidityTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        uvRiskTextView = findViewById(R.id.uvRiskTextView);
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        day2 = findViewById(R.id.day2);
        minimumTextView = findViewById(R.id.minimumTextView);
        maximumTextView = findViewById(R.id.maximumTextView);
        day3 = findViewById(R.id.day3);
        minTextView = findViewById(R.id.minTextView);
        maxTextView = findViewById(R.id.maxTextView);

        // Fetch weather forecast data
        fetchWeatherForecast();
    }

    private void fetchWeatherForecast() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create URL object for fetching weather forecast data
                    URL url = new URL(MainActivity.threeDayapiUrls[0]);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();

                        // Parse the XML response using WeatherForecastXmlPullParser
                        WeatherForecastXmlPullParser parser = new WeatherForecastXmlPullParser();
                        List<WeatherForecast> weatherForecasts = parser.parse(convertInputStreamToString(inputStream));

                        // Close the input stream
                        inputStream.close();
                        connection.disconnect();

                        // Update UI elements with weather forecast data
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI(weatherForecasts);
                            }
                        });
                    }
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Helper method to convert InputStream to String
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private void updateUI(List<WeatherForecast> weatherForecasts) {
        if (weatherForecasts != null && !weatherForecasts.isEmpty()) {
            // Display weather forecast for the first day
            WeatherForecast firstDayForecast = weatherForecasts.get(0);
            temperatureTextView.setText("Minimum Temperature: " + firstDayForecast.getMinTemperature());
            temperatureTextView1.setText("Maximum Temperature: " + firstDayForecast.getMaxTemperature());
            humidityTextView.setText(firstDayForecast.getHumidity());
            windSpeedTextView.setText(firstDayForecast.getWindSpeed());
            uvRiskTextView.setText(firstDayForecast.getUvRisk());
            titleTextView.setText(firstDayForecast.getTitle());
            descriptionTextView.setText("Sunset is at: " + firstDayForecast.getSunset());
            if (weatherForecasts.size() > 1) {
                WeatherForecast secondDayForecast = weatherForecasts.get(1);
                day2.setText(secondDayForecast.getTitle());
                minimumTextView.setText(secondDayForecast.getMinTemperature());
                maximumTextView.setText(secondDayForecast.getMaxTemperature());
            }
            if (weatherForecasts.size() > 2) {
                WeatherForecast thirdDayForecast = weatherForecasts.get(2);
                day3.setText(thirdDayForecast.getTitle());
                minTextView.setText(thirdDayForecast.getMinTemperature());
                maxTextView.setText(thirdDayForecast.getMaxTemperature());
            }
        } else {
            // Handle case where weather forecast data is missing
            temperatureTextView.setText("Weather forecast data not available");
            temperatureTextView1.setText("Weather forecast data not available");
            humidityTextView.setText("Weather forecast data not available");
            windSpeedTextView.setText("Weather forecast data not available");
            uvRiskTextView.setText("Weather forecast data not available");
            titleTextView.setText("Weather forecast data not available");
            descriptionTextView.setText("Weather forecast data not available");
        }

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v){
            // Create an Intent to navigate to the FutureForecastActivity
                Intent intent = new Intent(FutureForecastActivity.this, WeatherFieldActivity.class);

             // Start the FutureForecastActivity
            startActivity(intent);
    }
    });
}
}
