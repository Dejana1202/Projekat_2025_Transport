package com.example.projekat;

import com.example.projekat.controllers.CountryController;
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
    private static CountryController countryController;

    @Override
    public void start(Stage stage) throws IOException
    {

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("country.fxml"));

        Parent root = loader.load();
//        mainController = loader.getController();
        countryController = loader.getController();

//       System.out.println("Main initialized." + (mainController != null));
        System.out.println("Country initialized." + (countryController != null));

        stage.setTitle("Simulation");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public static void main(String[] args) {
       // TransportDataGenerator generator = new TransportDataGenerator(2,2);

        launch();
    }
}