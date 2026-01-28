package com.example.projekat.controllers;

import com.example.projekat.algorithms.RouteComparator;
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
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoreRoutesController {

    @FXML
    private Button buyCardButton1;
    @FXML
    private Button buyCardButton2;
    @FXML
    private Button buyCardButton3;
    @FXML
    private Button buyCardButton4;
    @FXML
    private Button buyCardButton5;
    @FXML
    private TableColumn<Route, String> fromColumn1;
    @FXML
    private TableColumn<Route, String> fromColumn2;
    @FXML
    private TableColumn<Route, String> fromColumn3;
    @FXML
    private TableColumn<Route, String> fromColumn4;
    @FXML
    private TableColumn<Route, String> fromColumn5;
    @FXML
    private TableColumn<Route, Number> priceColumn1;
    @FXML
    private TableColumn<Route, Number> priceColumn2;
    @FXML
    private TableColumn<Route, Number> priceColumn3;
    @FXML
    private TableColumn<Route, Number> priceColumn4;
    @FXML
    private TableColumn<Route, Number> priceColumn5;
    @FXML
    private TableView<Route> tableView1;
    @FXML
    private TableView<Route> tableView2;
    @FXML
    private TableView<Route> tableView3;
    @FXML
    private TableView<Route> tableView4;
    @FXML
    private TableView<Route> tableView5;
    @FXML
    private TableColumn<Route, String> toColumn1;
    @FXML
    private TableColumn<Route, String> toColumn2;
    @FXML
    private TableColumn<Route, String> toColumn3;
    @FXML
    private TableColumn<Route, String> toColumn4;
    @FXML
    private TableColumn<Route, String> toColumn5;
    @FXML
    private TableColumn<Route, String> typeColumn1;
    @FXML
    private TableColumn<Route, String> typeColumn2;
    @FXML
    private TableColumn<Route, String> typeColumn3;
    @FXML
    private TableColumn<Route, String> typeColumn4;
    @FXML
    private TableColumn<Route, String> typeColumn5;
    @FXML
    private Label totalLabel1;
    @FXML
    private Label totalLabel2;
    @FXML
    private Label totalLabel3;
    @FXML
    private Label totalLabel4;
    @FXML
    private Label totalLabel5;
    private List<List<Route>> routesList = new ArrayList<>();
    @FXML
    void onBuyCardPressed(ActionEvent event) {
            Object eventSource = event.getSource();

            int index = -1;
            if (eventSource == buyCardButton1) index = 0;
            else if (eventSource == buyCardButton2) index = 1;
            else if (eventSource == buyCardButton3) index = 2;
            else if (eventSource == buyCardButton4) index = 3;
            else if (eventSource == buyCardButton5) index = 4;

            if (index == -1) return;

            List<Route> chosenRoutes = (routesList.size()>index)? routesList.get(index) : null;
            if (chosenRoutes == null || chosenRoutes.isEmpty()){
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Ruta nije dostupna");
                a.showAndWait();
                return;
            }

            Bill bill = prepareBillFromRoutes(chosenRoutes);
            boolean ok = SerializationUtil.saveBill(bill, "./reports");
            boolean textOk = BillUtil.saveBillToTxt(bill, "./bills");
    }

    public void setRoutes(List<List<Route>> routesList){
        this.routesList = new ArrayList<>();
        for(int i=0; i<5; i++){
            if (routesList!= null && routesList.size()>i && routesList.get(i)!=null){
                this.routesList.add(routesList.get(i));
            }
            else {
                this.routesList.add(new ArrayList<>());
            }
        }

        List<TableView<Route>> tables = Arrays.asList(tableView1, tableView2, tableView3, tableView4, tableView5);
        List<TableColumn<Route,String>> fromCols = Arrays.asList(fromColumn1, fromColumn2, fromColumn3, fromColumn4, fromColumn5);
        List<TableColumn<Route,String>> toCols = Arrays.asList(toColumn1, toColumn2, toColumn3, toColumn4, toColumn5);
        List<TableColumn<Route,String>> typeCols = Arrays.asList(typeColumn1, typeColumn2, typeColumn3, typeColumn4, typeColumn5);
        List<TableColumn<Route,Number>> priceCols = Arrays.asList(priceColumn1, priceColumn2, priceColumn3, priceColumn4, priceColumn5);
        List<Label> totals = Arrays.asList(totalLabel1, totalLabel2, totalLabel3, totalLabel4, totalLabel5);

        for (int i=0; i<5; i++){
            List<Route> list = this.routesList.get(i);
            TableView<Route> tableView = tables.get(i);
            if (tableView == null) continue;

            // column for current table :
            configureTable(tableView, fromCols.get(i), toCols.get(i), typeCols.get(i), priceCols.get(i));

            ObservableList<Route> observableList = FXCollections.observableArrayList(list);
            tableView.setItems(observableList);

            int totalPrice = calculateTotalPrice(list);
            int totalMinutes = calculateTotalMinutes(list);
            String formatted = formatMinutes(totalMinutes);
            Label label = totals.get(i);
            if (label!=null){
                label.setText("Ukupno : " + formatted + ", " + totalPrice + " novčanih jedinica.");
            }
        }

    }

    private void configureTable(TableView<Route> table, TableColumn<Route, String> fromCol, TableColumn<Route, String> toCol, TableColumn<Route, String> typeCol, TableColumn<Route, Number> priceCol){
        if (fromCol !=null){
            fromCol.setCellValueFactory(cell ->
                    new SimpleStringProperty(formatDepartureCode(cell.getValue().getSource(),
                            cell.getValue().getType())));
        }
        if (toCol != null) {
            toCol.setCellValueFactory(cell ->
                    new SimpleStringProperty(cell.getValue().getDestination() + " (" + cell.getValue().getDepartureTime() + ")"));
        }
        if (typeCol != null) {
            typeCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getType()));
        }
        if (priceCol != null) {
            priceCol.setCellValueFactory(cell -> {
                return new SimpleIntegerProperty(cell.getValue().getPrice());
            });
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

    private Bill prepareBillFromRoutes(List<Route> routes){
        if (routes == null || routes.isEmpty()){
            return new Bill(new ArrayList<>(), LocalDateTime.now(), 0);
        }
        List<String> relations = new ArrayList<>();
        int totalPrice = 0;

        for (Route r: routes){
            if (r == null) continue;

            String formatSource = formatDepartureCode(r.getSource(), r.getType());
            String destination = r.getDestination()!=null?r.getDestination():"";
            relations.add(formatSource+" : " + destination);
            totalPrice+=r.getPrice();
        }
        return new Bill(relations, LocalDateTime.now(), totalPrice);
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
        return RouteComparator.calculateTotalTripDuration(routes);
    }
    private String formatMinutes(int totalMinutes){
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return hours + "h " + minutes + "min";
    }
}
