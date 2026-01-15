package com.example.projekat.controllers;

import com.example.projekat.models.Route;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;
import java.util.List;

public class RouteController {
    @FXML
    private TableView<Route> tableView;
    @FXML
    private TableColumn<Route, String> fromColumn;

    @FXML
    private TableColumn<Route, Number> priceColumn;

    @FXML
    private TableColumn<Route, String> toColumn;

    @FXML
    private TableColumn<Route, String> typeColumn;
    private List<Route> routes;

    public void setRoutes(List<Route> routes){
        this.routes = routes;
        ObservableList<Route> observableList = FXCollections.observableList(routes);
        tableView.setItems(observableList);

        fromColumn.setCellValueFactory(
                cell ->
                        new SimpleStringProperty(
                                formatDepartureCode(cell.getValue().getSource(),
                                        cell.getValue().getType())
                        )
        );
        toColumn.setCellValueFactory(
                cell ->
                        new SimpleStringProperty(
                                cell.getValue().getDestination()

                        )
        );
        typeColumn.setCellValueFactory(
                cell ->
                        new SimpleStringProperty(cell.getValue().getType())

        );
        priceColumn.setCellValueFactory(
                cell ->
                        new SimpleIntegerProperty(cell.getValue().getPrice())
        );
    }

    private String formatDepartureCode(String nodeId, String transportType){
        if (nodeId == null) return "";
        String coords = nodeId;
        if (coords.startsWith("G_")) {
            coords = coords.substring(2);
        }
        String prefix = "A";
        if (transportType != null && transportType.toLowerCase().contains("voz")){
            prefix = "Z";
        }
        return prefix + "_" + coords;
    }

}
