package com.example.projekat.models;

import org.graphstream.graph.Node;

import java.util.List;

public class Route {
    private String source, destination, type;
    private int price;
    private List<String> path;
    private String departureTime;
    public Route(){
        super();
    }

    public Route(String source, String destination, String type, int price, String departureTime) {
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.price = price;
        this.departureTime = departureTime;
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

    @Override
    public String toString(){
        return "Route { " + "source = " + source +
                '\'' + ", destination='" + destination + '\'' +
                ", type='" + type + '\'' + ", price=" + price +
                ", path=" + path + ", departure time=" +  departureTime +'}';
    }
}
