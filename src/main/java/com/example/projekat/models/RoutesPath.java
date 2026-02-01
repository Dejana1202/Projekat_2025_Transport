package com.example.projekat.models;

import javafx.scene.Node;

import java.util.List;

/**
 * Pomocna klasa za putanje
 */
public class RoutesPath {
    private List<Node> nodes;
    private List<Route> routes;
    private int totalCost;

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

}
