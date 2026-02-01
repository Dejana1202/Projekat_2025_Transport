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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Kontroler za prikaz najbolje Dijkstrine rute.
 */
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

    /**
     * Postavljanje podataka o listi ruta u tabelu za prikaz.
     * @param routes lista svih ruta formiranih kroz sve cvorove od izvorista do odredista, sa informacijama o putovanju.
     */
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
    /**
     * Pomocna metoda za konverziju formata G_X_Y u (A ili Z)_X_Y u zavisnosti od tipa(vrste) prevoza
     * @param nodeId cvor tj. grad
     * @param transportType voz ili autobus
     * @return String format (A ili Z)_X_Y
     */
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
    /**
     * Obrada klika za kupovinu karte.
     * Serijalizacija racuna @see {@link SerializationUtil} i cuvanje racuna u tekstualnom obliku @see {@link BillUtil}
     * @param event klik na dugme.
     */
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
    /**
     * Kreiranje racuna tj. objekta tipa @see {@link Bill}
     */
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
    /**
     * Pomocna metoda za prikaz ukupne cijene datog putovanja na Labeli unutar tabele vezane za to putovanje
     * @param routes Rute, kroz koje se prolazi da bi se sabrale sve cijene medjugradova i formirala ukupna putovanja
     * @return ukupna cijena datog putovanja
     */
    private int calculateTotalPrice(List<Route> routes){
        if (routes== null)return 0;
        int totalPrice = 0;
        for (Route r : routes){
            totalPrice+=Math.max(0, r.getPrice());
        }
        return totalPrice;
    }
    /**
     * Pomocna metoda za prikaz ukupnog vremena  datog putovanja na Labeli unutar tabele vezane za to putovanje
     * @param routes Rute, kroz koje se prolazi da bi se sabralo svo vrijeme u minutama svih medjugradova i formiralo ukupno vrijeme provedeno u prevozu
     * @return ukupno vrijeme provedeno tokom datog putovanja
     */
    private int calculateTotalMinutes(List<Route> routes){
        return RouteComparator.calculateTotalTripDuration(routes);
    }
    /**
     * String reprezentacija ukupnog broja minuta u obliku "NhMmin" gdje N predstavlja broj sati, a M minuta.
     * @param totalMinutes ukupan broj minuta
     * @return String oblik za sate i minute
     */
    private String formatMinutes(int totalMinutes){
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return hours + "h " + minutes + "min";
    }
}
