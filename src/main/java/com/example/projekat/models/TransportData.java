package com.example.projekat.models;


import java.util.List;

/**
 * TransportData je drzava formirana u obliku matrice, sa stanicama i polascima izmedju njih.
 */
public class TransportData
{
    /**
     * Mapa drzave stanicama
     */
    private String[][] countryMap;
    /**
     * Lista svih stanica u drzavi
     */
    private List<Station> stations;
    /**
     * Lista polazaka izmedju stanica
     */
    private List<Departure> departures;

    public String[][] getCountryMap() {
        return countryMap;
    }

    public void setCountryMap(String[][] countryMap) {
        this.countryMap = countryMap;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Departure> getDepartures() {
        return departures;
    }

    public void setDepartures(List<Departure> departures) {
        this.departures = departures;
    }

    public TransportData()
    {
    }

    /**
     * Konstruktor za kreiranje mape gradova sa listom stanica i listom polazaka izmedju stanica
     */
    public TransportData(String[][] countryMap, List<Station> stations, List<Departure> departures) {
        super();
        this.countryMap = countryMap;
        this.stations = stations;
        this.departures = departures;
    }
}
