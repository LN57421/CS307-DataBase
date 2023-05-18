package com.example.databasefinalproject.Entity;

public class City {
    private String cityName;

    private String cityState;

    public String getCityName() {
        return cityName;
    }

    public String getCityState() {
        return cityState;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityName='" + cityName + '\'' +
                ", cityState='" + cityState + '\'' +
                '}';
    }
}
