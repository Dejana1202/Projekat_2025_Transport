package com.example.projekat.models;

public class Station
{
    private String city;
    private String busStation;
    private String trainStation;

    public Station(String city, String busStation, String trainStation)
    {
        super();
        this.city = city;
        this.busStation = busStation;
        this.trainStation = trainStation;
    }
    public Station()
    {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBusStation() {
        return busStation;
    }

    public void setBusStation(String busStation) {
        this.busStation = busStation;
    }

    public String getTrainStation() {
        return trainStation;
    }

    public void setTrainStation(String trainStation) {
        this.trainStation = trainStation;
    }
}
