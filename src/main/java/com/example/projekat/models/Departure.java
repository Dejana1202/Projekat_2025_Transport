package com.example.projekat.models;

/**
 * Klasa Departure je jedan autobuski ili vozni polazak iz jednog grada u drugi, sa odredjenim vremeno polaska, trajanja i minimalnim vremenom transfera.
 * Svaki polazak ima svoju cijenu.
 */
public class Departure {
    /**
     * Tip polaska moze biti autobus ili voz
     */
    private String type;
    /**
     * Stanica iz koje se polazi
     */
    private String from;
    /**
     * Odredisni grad
     */
    private String to;
    /**
     * Vrijeme polaska
     */
    private String departureTime;
    /**
     * Trajanje putovanja u minutama
     */
    private int duration;
    /**
     * Cijena putovanja
     */
    private int price;
    /**
     * Najmanje potrebno vrijeme za transfer
     */
    private int minTransferTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMinTransferTime() {
        return minTransferTime;
    }

    public void setMinTransferTime(int minTransferTime) {
        this.minTransferTime = minTransferTime;
    }

    public Departure() {
    }

    /**
     * Konstruktor za formiranje polaska
     */
    public Departure(String type, String from, String to, String departureTime, int duration, int price, int minTransferTime) {
        super();
        this.type = type;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.duration = duration;
        this.price = price;
        this.minTransferTime = minTransferTime;
    }
}
