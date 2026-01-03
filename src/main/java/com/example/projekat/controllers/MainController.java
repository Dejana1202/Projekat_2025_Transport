package com.example.projekat.controllers;
import com.example.projekat.generator.TransportDataGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {
    public static CountryController countryController;

    @FXML
    private TextField mField;

    @FXML
    private TextField nField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button showMapBtn;

    @FXML
    public void generate(){
        try {
            int m = Integer.parseInt(mField.getText());
            int n = Integer.parseInt(nField.getText());

            new TransportDataGenerator(m, n);

            messageLabel.setText("JSON fajl je generisan!");
            showMapBtn.setVisible(true);
        }
        catch (NumberFormatException e)
        {
            messageLabel.setText("Greska. Dimenzije m i n moraju biti cijeli brojevi.");
        }
    }

    @FXML
    public void showMap(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projekat/country.fxml"));
            Parent root = loader.load();
            countryController = loader.getController();

            Stage newStage = new Stage();
            newStage.setTitle("Country Map");
            newStage.setScene(new Scene(root));
            newStage.show();

            Stage currentStage = (Stage) mField.getScene().getWindow();
            currentStage.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
