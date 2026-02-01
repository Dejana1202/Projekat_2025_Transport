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

    /**
     * FXML metoda, povezana sa GUI dijelom aplikacije. Formira se matrica gradova, dimenzija nxm.
     * @see TransportDataGenerator kreira gradove i polaske izmedju njih u obliku matrice dimenzija nxm.
     * Generisane podatke smjesta u JSON fajl.
     * Sve ispisuje na GUI.
     */
    @FXML
    public void generate(){
        try {

            int m = Integer.parseInt(mField.getText());
            int n = Integer.parseInt(nField.getText());

            CountryController.setM(m);
            CountryController.setN(n);
            if (m>1 && m<100 && n>1 && n<100){

                new TransportDataGenerator(m, n);
                CountryController.setM(2);
                CountryController.setN(2);
                messageLabel.setText("JSON fajl je generisan!");
                showMapBtn.setVisible(true);
            }
            else {
                messageLabel.setText("Dimenzije m i n moraju biti izmedju 1 i 100.");
            }
        }
        catch (NumberFormatException e)
        {
            messageLabel.setText("Greska. Dimenzije m i n moraju biti cijeli brojevi.");
        }
    }

    /**
     * Promjena scene. Prikaz mape gradova.
     */
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
