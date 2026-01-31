package com.example.projekat.models;

import org.graphstream.graph.Node;

import java.util.List;

/**
 * Podaci o jednoj ruti polaska izmedju dva grada tj. dvije stanice.
 * Lista ovih ruta cini jednu putanju izmedju bilo koja dva grada.
 */
public class Route {
    private String source, destination, type;
    private int price;
    private List<String> path;
    private String departureTime;
    private int durationMinutes;
    private int minTransferTime;

    public Route(){
        super();
    }

    /**
     * Konstruktor za generisanje rute izmedju dva grada.
     * @param source naziv stanice, iz koje se polazi
     * @param destination naziv grada, u koji se ide
     * @param type tip prevoza : autobus ili voz
     * @param price cijena voznje
     * @param departureTime vrijeme polaska
     * @param durationMinutes trajanje voznje u minutama
     * @param minTransferTime minimalno vrijeme za transfer
     */
    public Route(String source, String destination, String type, int price, List<String> path, String departureTime, int durationMinutes, int minTransferTime) {
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.price = price;
        this.path = path;
        this.departureTime = departureTime;
        this.durationMinutes = durationMinutes;
        this.minTransferTime = minTransferTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getMinTransferTime() {
        return minTransferTime;
    }

    public void setMinTransferTime(int minTransferTime) {
        this.minTransferTime = minTransferTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * toString metoda, koja vraca @return String reprezentaciju objekta tipa Route
     */
    @Override
    public String toString(){
        return "Route { " + "source = " + source +
                '\'' + ", destination='" + destination + '\'' +
                ", type='" + type + '\'' + ", price=" + price +
                ", path=" + path + ", departure time=" +  departureTime +'}';
    }
}
