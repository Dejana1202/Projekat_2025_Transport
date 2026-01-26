package com.example.projekat.controllers;

import com.example.projekat.models.Bill;
import com.example.projekat.models.Route;
import com.example.projekat.utils.BillUtil;
import com.example.projekat.utils.SerializationUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @FXML
    private Button buyCardButton;
    @FXML
    private Label totalLabel;
    private List<Route> routes;
    private Bill currentBill;

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
                                cell.getValue().getDestination() + "(" + cell.getValue().getDepartureTime() +")"

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
        int totalPrice = calculateTotalPrice(routes);
        int totalMinutes = calculateTotalMinutes(routes);
        String formatted = formatMinutes(totalMinutes);

        if (totalLabel!=null){
            totalLabel.setText("Ukupno : " + formatted + ", " + totalPrice + " novčanih jedinica.");
        }
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
    @FXML
    void onBuyCardPressed(ActionEvent event) {
        if (currentBill == null){
            prepareBillFromRoutes();
        }
        System.out.println(currentBill);
        // to txt file - ser.
        boolean ok = SerializationUtil.saveBill(currentBill, "./reports");
        if (ok){
            System.out.println("Racun sacuvan.");
        }
        else{
            System.out.println("Greska pri cuvanju racuna.");
        }
        boolean txtOk = BillUtil.saveBillToTxt(currentBill, "./bills");
        if (txtOk){
            System.out.println("Racun sacuvan u txt formatu.");
        } else {
            System.out.println("Greska pri cuvanju racuna u txt formatu.");
        }
    }
    private void prepareBillFromRoutes(){
        if (this.routes == null || this.routes.isEmpty()){
            currentBill = new Bill(new ArrayList<>(), LocalDateTime.now(), 0);
            return;
        }
        List<String> relations = new ArrayList<>();
        int totalPrice = 0;

        for (Route r: this.routes){
            if (r==null) continue;
            String formattedSource = formatDepartureCode(r.getSource(), r.getType());
            String destination = r.getDestination() != null? r.getDestination(): "";
            relations.add(formattedSource + " : " + destination);
            totalPrice+= r.getPrice();
        }
        LocalDateTime now = LocalDateTime.now();
        currentBill=new Bill(relations, now, totalPrice);
    }
    private int calculateTotalPrice(List<Route> routes){
        if (routes== null)return 0;
        int totalPrice = 0;
        for (Route r : routes){
            totalPrice+=Math.max(0, r.getPrice());
        }
        return totalPrice;
    }
    private int calculateTotalMinutes(List<Route> routes){
        if (routes == null) return 0;
        int totalMinutes = 0;
        for (Route r : routes){
            totalMinutes+=r.getDurationMinutes();
            totalMinutes+=r.getMinTransferTime();
        }
        return totalMinutes;
    }
    private String formatMinutes(int totalMinutes){
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return hours + "h " + minutes + "min";
    }
}
