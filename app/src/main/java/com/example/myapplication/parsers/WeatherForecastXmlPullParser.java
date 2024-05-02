package com.example.myapplication.parsers;

import android.util.Log;
import android.util.Xml;

import com.example.myapplication.objects.WeatherForecast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WeatherForecastXmlPullParser {
    // We don't use namespaces
    private static final String ns = null;

    public List<WeatherForecast> parse(String inputString) throws XmlPullParserException, IOException {
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


    private List<WeatherForecast> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<WeatherForecast> entries = new ArrayList<>();

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

    private WeatherForecast readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String description = null;
        String pubDate = null;
        String mintemperature = null;
        String maxtemperature = null;
        String windSpeed = null;
        String humidity = null;
        String pressure = null;
        String visibility = null;
        String windDirection = null;
        String sunset = null;
        String sunrise = null;
        String uvRisk = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "title":
                // Read the title and extract desired text
                title = readText(parser);
                String[] titleParts = title.split(",");
                if (titleParts.length > 0) {
                    title = titleParts[0].trim();
                }
                break;
                case "description":
                    // Split the description by comma and assign values accordingly
                    String[] descriptionParts = readText(parser).split(",");
                    for (String part : descriptionParts) {
                        part = part.trim();
                        if (part.startsWith("Minimum Temperature:")) {
                            mintemperature = part.replace("Minimum Temperature:", "").trim();
                        } else if (part.startsWith("Maximum Temperature:")) {
                            maxtemperature = part.replace("Maximum Temperature:", "").trim();
                        } else if (part.startsWith("Wind Direction:")) {
                            windDirection = part.replace("Wind Direction:", "").trim();
                        } else if (part.startsWith("Wind Speed:")) {
                            windSpeed = part.replace("Wind Speed:", "").trim();
                        } else if (part.startsWith("Humidity:")) {
                            humidity = part.replace("Humidity:", "").trim();
                        } else if (part.startsWith("Pressure:")) {
                            pressure = part.replace("Pressure:", "").trim();
                        } else if (part.startsWith("Visibility:")) {
                            visibility = part.replace("Visibility:", "").trim();
                        } else if (part.startsWith("Sunrise:")) {
                            sunrise = part.replace("Sunrise:", "").trim();
                        } else if (part.startsWith("Sunset:")) {
                            sunset = part.replace("Sunset:", "").trim();
                        } else if (part.startsWith("UV Risk:")) {
                            uvRisk = part.replace("UV Risk:", "").trim();
                        }
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
        Log.d("Parser", "Title: " + title);
        Log.d("Parser", "Description: " + description);
        Log.d("Parser", "PubDate: " + pubDate);
        Log.d("Parser", "Min Temperature: " + mintemperature);
        Log.d("Parser", "Max Temperature: " + maxtemperature);
        Log.d("Parser", "WindSpeed: " + windSpeed);
        Log.d("Parser", "Humidity: " + humidity);
        Log.d("Parser", "Pressure: " + pressure);
        Log.d("Parser", "Visibility: " + visibility);
        Log.d("Parser", "WindDirection: " + windDirection);
        Log.d("Parser", "UV Risk: " + uvRisk);
        Log.d("Parser", "Sunset: " + sunset);
        Log.d("Parser", "Sunrise: " + sunrise);
        return new WeatherForecast(title, description, pubDate, mintemperature, maxtemperature, windSpeed, humidity, pressure, visibility, windDirection, uvRisk, sunset, sunrise);
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

















//package com.example.myapplication.parsers;
//
//import android.util.Log;
//
//import com.example.myapplication.objects.WeatherForecast;
//
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public class WeatherForecastXmlPullParser {
//    public static List<WeatherForecast> parse(InputStream inputStream) throws XmlPullParserException, IOException {
//        List<WeatherForecast> forecasts = new ArrayList<>();
//        WeatherForecast currentForecast = null;
//
//        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//        XmlPullParser parser = factory.newPullParser();
//        parser.setInput(inputStream, null);
//
//        int eventType = parser.getEventType();
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//            String tagName = parser.getName();
//            switch (eventType) {
//                case XmlPullParser.START_TAG:
//                    if (tagName.equalsIgnoreCase("item")) {
//                        String title = "";
//                        String description = "";
//                        String pubDate = "";
//                        String minTemperature = "";
//                        String maxTemperature = "";
//                        String windSpeed = "";
//                        String humidity = "";
//                        String pressure = "";
//                        String visibility = "";
//                        String windDirection = "";
//                        String uvRisk = "";
//                        String sunset = "";
//                        String sunrise = "";
//                        currentForecast = new WeatherForecast(
//                                title,
//                                description,
//                                pubDate,
//                                minTemperature,
//                                maxTemperature,
//                                windSpeed,
//                                humidity,
//                                pressure,
//                                visibility,
//                                windDirection,
//                                uvRisk,
//                                sunset,
//                                sunrise
//                        );
//                    } else if (currentForecast != null) {
//                        if (tagName.equalsIgnoreCase("title")) {
//                            currentForecast.setTitle(parser.nextText());
//                        } else if (tagName.equalsIgnoreCase("description")) {
//                            String description = parser.nextText();
//                            parseDescription(description, currentForecast);
//                        }
//                    }
//                    break;
//                case XmlPullParser.END_TAG:
//                    if (tagName.equalsIgnoreCase("item") && currentForecast != null) {
//                        forecasts.add(currentForecast);
//                    }
//                    break;
//            }
//            eventType = parser.next();
//        }
//        Log.d("Future XMLParser", "parse: The weather: " + forecasts);
//        return forecasts;
//    }
//
//    private static void parseDescription(String description, WeatherForecast forecast) {
//        // Split the description string into separate pieces of information
//        String[] parts = description.split(", ");
//
//        // Iterate over each piece of information and parse it accordingly
//        for (String part : parts) {
//            if (part.contains("Minimum Temperature")) {
//                forecast.setMinTemperature(getValueFromDescription(part));
//            } else if (part.contains("Maximum Temperature")) {
//                forecast.setMaxTemperature(getValueFromDescription(part));
//            } else if (part.contains("Wind Direction")) {
//                forecast.setWindDirection(getValueFromDescription(part));
//            } else if (part.contains("Wind Speed")) {
//                forecast.setWindSpeed(getValueFromDescription(part));
//            } else if (part.contains("Visibility")) {
//                forecast.setVisibility(getValueFromDescription(part));
//            } else if (part.contains("Pressure")) {
//                forecast.setPressure(getValueFromDescription(part));
//            } else if (part.contains("Humidity")) {
//                forecast.setHumidity(getValueFromDescription(part));
//            } else if (part.contains("UV Risk")) {
//                forecast.setUvRisk(getValueFromDescription(part));
//            } else if (part.contains("Sunrise")) {
//                forecast.setSunrise(getValueFromDescription(part));
//            } else if (part.contains("Sunset")) {
//                forecast.setSunset(getValueFromDescription(part));
//            }
//        }
//        Log.d("Weather Parsed", "parseDescription: " + parts);
//    }
//
//    private static String getValueFromDescription(String part) {
//        // Split the part into key and value
//        String[] keyValue = part.split(": ");
//        if (keyValue.length == 2) {
//            return keyValue[1];
//        }
//        return "";
//    }
//}
//
