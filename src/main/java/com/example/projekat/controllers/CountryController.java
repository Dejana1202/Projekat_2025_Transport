package com.example.projekat.controllers;

import com.example.projekat.models.Criteria;
import com.example.projekat.models.Departure;
import com.example.projekat.models.Station;
import com.example.projekat.models.TransportData;
import com.example.projekat.utils.TransportDataUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.Viewer;

import java.util.List;

import java.util.*;

public class CountryController {
    public static final String FILENAME = "transport_data.json";
    private static int m, n;
    private double graphWidth = 1200;
    private double graphHeight = 800;

    @FXML
    private ComboBox<String> criteriaCombo;

    @FXML
    private ComboBox<String> fromCombo;

    @FXML
    private StackPane graphContainer;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ComboBox<String> toCombo;
    @FXML
    private Label statusLabel;

    private Graph graph;
    private TransportData data;
    private Map<String, Station> stationMap = new HashMap<>();
    @FXML
    private Label totalAmountLabel;
    @FXML
    private Label totalTicketsLabel;
    @FXML
    private Button searchButton;
    @FXML
    private Button showRouteTableButton;
    private String selectedFrom = null;
    private String selectedTo = null;
    private Criteria selectedCriteria = null;
    @FXML
    public void initialize(){
        progressIndicator.setVisible(false);
        criteriaCombo.getItems().addAll("Najkraće vrijeme putovanja", "Najniža cijena", "Najmanji broj presjedanja");
        loadGraph(FILENAME);
    }
    @FXML
    private void onFromSelected(){
        selectedFrom = fromCombo.getValue();
        System.out.println("Odrediste : " + selectedFrom);
    }
    @FXML
    private void onToSelected(){
        selectedTo = toCombo.getValue();
        System.out.println("Polaziste : " + selectedTo);

    }
    @FXML
    private void onCriteriaSelected(){
    int index = criteriaCombo.getSelectionModel().getSelectedIndex();
    if (index == 0){
        selectedCriteria = Criteria.FASTEST;
    }
    else if (index == 1){
        selectedCriteria = Criteria.CHEAPEST;
    }
    else if (index == 2){
        selectedCriteria = Criteria.LEAST_TRANSFERS;
    }
        System.out.println("Izabrani kriterijum " + selectedCriteria.name());
    }
    @FXML
    void onBestRoutePressed(ActionEvent event) {

    }
    @FXML
    void onSearchPressed(ActionEvent event) {
        if (selectedFrom != null && selectedTo != null && selectedCriteria != null){
            System.out.println("Pretrazujemo Dijkstra...");
        }
        else {
            System.out.println("Molim vas, odaberite polaziste, odrediste i kriterijum.");
        }
    }

    private void loadGraph(String fileName){
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("JSON se učitava...");
                data= TransportDataUtil.readFromFile(fileName);

                updateMessage("Mapiranje stanica...");
                for (Station s : data.getStations()){
                    stationMap.put(s.getCity(), s);
                }
                updateMessage("Kreiranje grafa...");
                buildGraph(data);

                updateMessage("Gotovo");
                return null;
            }
        };

    task.setOnRunning(e ->
    {
        progressIndicator.setVisible(true);
        statusLabel.setText("Učitavanje...");
    });

    task.messageProperty().addListener((obs, oldV, newV)->
                    Platform.runLater(()->
                            statusLabel.setText(newV)
                            )
            );

    task.setOnSucceeded(
            e->
            {
                progressIndicator.setVisible(false);
                statusLabel.setText("Spreman");
                Platform.runLater(
                        ()-> embedGraphView()
                );
                fillComboboxes();
            }
    );

    task.setOnFailed(
            e->{
                progressIndicator.setVisible(false);
                statusLabel.setText("Greska pri učitavanju" + task.getException().getMessage());
                task.getException().printStackTrace();
            }
    );
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    private void fillComboboxes(){
        TreeSet<String> cities = new TreeSet<>(stationMap.keySet());
        fromCombo.getItems().setAll(cities);
        toCombo.getItems().setAll(cities);
    }
    private void buildGraph(TransportData data){
        graph = new MultiGraph("country");

        graph.setStrict(false);
        graph.setAutoCreate(true);
       // graph.setAutoCreate(false);

        double spacing = 80.0;

        int cols = Math.max(1, CountryController.getM());
        int rows = Math.max(1, CountryController.getN());

        graphWidth = (cols + 1) * spacing;
        graphHeight = (rows + 1) * spacing;

        Map<String, String> stationCodeToCity = new HashMap<>();

        for (Station s : data.getStations()){
            String city = s.getCity();
            Node node = graph.addNode(city);

            node.setAttribute("ui.label", city);

            String[] parts = city.split("_");
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);

            double px = x*spacing;
            double py = y*spacing;

            node.setAttribute("xyz", px, py, 0.0);
            node.setAttribute("station", s);

            if (s.getBusStation()!=null) stationCodeToCity.put(s.getBusStation(), city);
            if (s.getTrainStation() != null) stationCodeToCity.put(s.getTrainStation(), city);
        }

       // int edgeCounter = 0;
        for (Departure d: data.getDepartures()){
            String fromCode = d.getFrom();
            String toCode = d.getTo();

            String fromCity = stationCodeToCity.get(fromCode);
            String toCity=toCode;

            if (fromCity == null || toCity == null) continue;

            String edgeId = fromCity + "->" + toCity;

            Edge e = graph.getEdge(edgeId);
            if (e == null){
                // nova usmjerena grana
                e = graph.addEdge(edgeId, fromCity, toCity, true);
                // i njeni atributi
                List<Departure> list  = new ArrayList<>();
                e.setAttribute("departures", list);
                e.setAttribute("minDuration", Integer.MAX_VALUE);
                e.setAttribute("minPrice", Integer.MAX_VALUE);
                e.setAttribute("departuresCount", 0);
                e.setAttribute("minTransferTime", d.getMinTransferTime());
            }

            // dodavanje departure u listu

            List<Departure> depList =  (List<Departure>) e.getAttribute("departures");
            depList.add(d);
            e.setAttribute("departures", depList);

            int minD = Math.min((Integer)e.getAttribute("minDuration"), d.getDuration());
            int minP = Math.min((Integer)e.getAttribute("minPrice"), d.getPrice());
            int cnt = (Integer)e.getAttribute("departuresCount") + 1;


            e.setAttribute("minDuration", minD);
            e.setAttribute("minPrice", minP);
            e.setAttribute("departuresCount", cnt);

            // da labela pokazuje minPrice
         //   e.setAttribute("ui.label", String.valueOf(e.getAttribute("minPrice")));

            System.out.println(edgeId);
            System.out.println("From: "+ fromCity + " To : "+ toCity + " - minPrice "+ minP);
           // int offset = (cnt - 1) * 12;
         //   e.setAttribute("ui.style", "text-offset: 0px, " + offset + "px;");

            List<Departure> deps = (List<Departure>) e.getAttribute("departures");
            if (deps != null) {
                for (Departure dep : deps) {
                    System.out.println("    Departure: "
                            + dep.getFrom() + " -> " + dep.getTo()
                            + ", price=" + dep.getPrice()
                            + ", duration=" + dep.getDuration());
                }
            }
        }

    }
    private void embedGraphView(){
        FxViewer viewer = new FxViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        FxViewPanel viewPanel = (FxViewPanel) viewer.addDefaultView(false);

        // viewPanel je postavljen u ScrollPane
        // zbog mogucnosti skrolovanja - Scroll

        viewPanel.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        viewPanel.setMinSize(0,0);
        ScrollPane scrollPane = new ScrollPane(viewPanel);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        scrollPane.prefViewportWidthProperty().bind(graphContainer.widthProperty());
        scrollPane.prefViewportHeightProperty().bind(graphContainer.heightProperty());

        graphContainer.getChildren().clear();
        graphContainer.getChildren().add(scrollPane);

       // graphContainer.getChildren().add(viewPanel);

        double nodeSize = (Math.max(CountryController.getM(), CountryController.getN()) > 60)?2:4;
        String style = "node { size: " + nodeSize + "px; fill-color: black; }" + "edge { size: 1px; fill-color: gray; }"
                + "graph { padding: 50px; }";

        graph.setAttribute("ui.stylesheet", style);

        Platform.runLater(() ->{

                    if(graphContainer.getScene() != null){
                        Stage stage = (Stage) graphContainer.getScene().getWindow();
                        stage.setMaximized(true);
                        //stage.setResizable(false);
                    }
        }
                );
    }
    public double getEdgeWeight(Edge e, Criteria criteria){
        Number dur = e.getNumber("duration");
        Number price = e.getNumber("price");
        Number transfers = e.getNumber("transfers");
        return switch (criteria){
            case FASTEST -> dur.doubleValue();
            case CHEAPEST -> price.doubleValue();
            case LEAST_TRANSFERS -> transfers.doubleValue();
            default -> dur.doubleValue();
        };
    }
    public static int getM() {
        return m;
    }

    public static void setM(int m) {
        CountryController.m = m;
    }

    public static int getN() {
        return n;
    }

    public static void setN(int n) {
        CountryController.n = n;
    }
}
