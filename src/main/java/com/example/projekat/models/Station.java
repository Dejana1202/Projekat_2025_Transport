package com.example.projekat.models;

/**
 * Station klasa je reprezentacija jednog grada, koji ima dvije stanice : autobusku i zeljeznicke.
 */
public class Station
{
    /**
     * Naziv grada
     */
    private String city;
    /**
     * Naziv autobuske stanice
     */
    private String busStation;
    /**
     * Naziv zeljeznicke stanice
     */
    private String trainStation;

    /**
     * Konstruktor, uz pomocu kog generisemo jedan grad
     * @param city naziv grada
     * @param busStation naziv autobuske stanice
     * @param trainStation naziv zeljeznicke stanice
     */
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
