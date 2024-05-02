package com.example.myapplication.objects;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CityNameExtractor {

    public static String extractCityName(Context context, String locationId) {
        String cityName = null;
        try (InputStream inputStream = context.getAssets().open("cities5000.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2 && parts[0].equals(locationId)) {
                    cityName = parts[1];
                    break; // Stop reading after finding the first matching location ID
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (cityName != null) {
            Log.d("CityNameExtractor", "Matched city: " + cityName + " for location ID: " + locationId);
        } else {
            Log.d("CityNameExtractor", "No matching city found for location ID: " + locationId);
        }

        return cityName;
    }
}
