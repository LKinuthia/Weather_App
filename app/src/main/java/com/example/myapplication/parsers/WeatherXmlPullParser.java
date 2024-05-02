package com.example.myapplication.parsers;

import android.util.Log;
import android.util.Xml;

import com.example.myapplication.objects.Weather;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WeatherXmlPullParser {

        // We don't use namespaces
        private static final String ns = null;

    public List<Weather> parse(String inputString) throws XmlPullParserException, IOException {
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }


        private List<Weather> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
            List<Weather> entries = new ArrayList<>();

            parser.require(XmlPullParser.START_TAG, ns, "rss");
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, ns, "channel");

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the <item> tag
                if (name.equals("item")) {
                    entries.add(readEntry(parser));
                } else {
                    skip(parser);
                }
            }
            return entries;
        }

        private Weather readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "item");
            String cityCode = null;
            String title = null;
            String description = null;
            String pubDate = null;
            String temperature = null;
            String windSpeed = null;
            String humidity = null;
            String pressure = null;
            String visibility = null;
            String windDirection = null;
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                switch (name) {
                    case "title":
                        title = readText(parser);
                        break;
                    case "description":
                        // Split the description by comma and assign values accordingly
                        String[] descriptionParts = readText(parser).split(",");
                        if (descriptionParts.length >= 1) {
                            String temperaturePart = descriptionParts[0].trim();
                            temperaturePart = temperaturePart.replace("Temperature:", "").trim();
                            temperaturePart = temperaturePart.replaceAll("\\(.*?\\)", "").trim(); // Remove Fahrenheit
                            temperature = temperaturePart;
                        }
                        if (descriptionParts.length >= 1){
                            windDirection = descriptionParts[1].trim();
                        }
                        if (descriptionParts.length >= 2) {
                            windSpeed = descriptionParts[2].trim();
                        }
                        if (descriptionParts.length >= 3) {
                            humidity = descriptionParts[3].trim();
                        }
                        if (descriptionParts.length >= 4) {
                            pressure = descriptionParts[4].trim();
                        }
                        if (descriptionParts.length >= 5) {
                            visibility = descriptionParts[5].trim();
                        }
                        break;
                    case "pubDate":
                        pubDate = readText(parser);
                        break;
                    default:
                        skip(parser);
                        break;
                }
            }

            // Log the parsed weather data
            Log.d("WeatherParser", "Title: " + title);
            Log.d("WeatherParser", "Description: " + description);
            Log.d("WeatherParser", "PubDate: " + pubDate);
            Log.d("WeatherParser", "Temperature: " + temperature);
            Log.d("WeatherParser", "WindSpeed: " + windSpeed);
            Log.d("WeatherParser", "Humidity: " + humidity);
            Log.d("WeatherParser", "Pressure: " + pressure);
            Log.d("WeatherParser", "Visibility: " + visibility);
            Log.d("WeatherParser", "WindDirection: " + windDirection);
            return new Weather(cityCode, title, description, pubDate, temperature, windSpeed, humidity, pressure, visibility, windDirection);
        }

        // Processes text nodes in the feed.
        private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }

        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }
    }

