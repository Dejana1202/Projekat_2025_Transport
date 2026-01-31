package com.example.projekat;

import com.example.projekat.controllers.CountryController;
import com.example.projekat.controllers.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Osnovna klasa, iz koje se pokrece program. Ucitava se GUI aplikacija sa pocetnim ekranom.
 * @author Dejana Culibrk
 * @version 1.0
 */
public class Main extends Application {
    private static MainController mainController;
    private static CountryController countryController;

    @Override
    public void start(Stage stage) throws IOException
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("country.fxml"));

        Parent root = loader.load();
       mainController = loader.getController();
   //     countryController = loader.getController();

       System.out.println("Main initialized." + (mainController != null));

        stage.setTitle("Simulation");
        stage.setScene(new Scene(root));
        stage.show();
    }
    public static void main(String[] args) {

        launch();
    }
}