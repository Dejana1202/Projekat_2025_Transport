package com.example.projekat.controllers;

import com.example.projekat.models.Route;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    void onBuyCardPressed(ActionEvent event) {

    }
}
