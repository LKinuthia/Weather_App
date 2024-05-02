package com.example.myapplication.objects;

public class WeatherForecast {
    private String title;
    private String description;
    private String pubDate;
    private String minTemperature;
    private String maxTemperature;
    private String windSpeed;
    private String humidity;
    private String pressure;
    private String visibility;
    private String windDirection;
    private String uvRisk;
    private String sunset;
    private String sunrise;


    public WeatherForecast(String title, String description, String pubDate, String minTemperature, String maxTemperature, String windSpeed, String humidity, String pressure, String visibility, String windDirection, String uvRisk, String sunset, String sunrise) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.pressure = pressure;
        this.visibility = visibility;
        this.windDirection = windDirection;
        this.uvRisk = uvRisk;
        this.sunset = sunset;
        this.sunrise = sunrise;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getUvRisk() {
        return uvRisk;
    }

    public void setUvRisk(String uvRisk) {
        this.uvRisk = uvRisk;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }
}
