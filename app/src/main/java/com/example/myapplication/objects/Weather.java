package com.example.myapplication.objects;


import java.io.Serializable;


public class Weather implements Serializable {
    private String locationId;
    private String title;
    private String description;
    private String pubDate;
    private String temperature;
    private String windSpeed;
    private String humidity;
    private String pressure;
    private String visibility;
    private String windDirection;

    // Constructor
    public Weather(String locationId, String title, String description, String pubDate,
                   String temperature, String windSpeed,
                   String humidity, String pressure, String visibility, String windDirection) {
        this.locationId = locationId;
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.pressure = pressure;
        this.visibility = visibility;
        this.windDirection = windDirection;
    }

    public String getlocationId(){
        return locationId;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getwindDirection(){
        return windDirection;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setwindDirection(String windDirection) {
        this.windDirection = windDirection;
    }


}
