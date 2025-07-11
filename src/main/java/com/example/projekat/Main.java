package com.example.projekat;

import com.example.projekat.controllers.MainController;
import com.example.projekat.generator.TransportDataGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static MainController mainController;

    @Override
    public void start(Stage stage) throws IOException
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        mainController = loader.getController();
        System.out.println("Main initialized." + (mainController != null));
        stage.setTitle("Simulation");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public static void main(String[] args) {
        TransportDataGenerator generator = new TransportDataGenerator(3,5);

        launch();
    }
}