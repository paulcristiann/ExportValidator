package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static javax.swing.JOptionPane.showMessageDialog;

public class ControllerMeniuPrincipal {

    static public File fisierXML;
    static public File fisierSemnat;
    public Button butonCitireXML;
    public Label labelFisier;
    public Button butonValidareSemnatura;
    public Label statusFisier;

    @FXML
    private void incarcaFisier(){

        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showOpenDialog(null);

        if(f.getName().contains(".xml")){

            fisierXML = f;
            butonValidareSemnatura.setVisible(false);
            statusFisier.setText("Fisierul incarcat este in format XML. Functia de validare a semnaturii nu se poate folosi!");

        }else {
            fisierSemnat = f;
            butonValidareSemnatura.setVisible(true);
        }
        labelFisier.setText(f.getName());

    }

    @FXML
    private void openCitireXML(){

        ReadXMLFile obiectReadXML = new ReadXMLFile();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Interfata Citire XML.fxml"));
        } catch (IOException e) {
                e.printStackTrace();
        }
            Stage curenta = (Stage) butonCitireXML.getScene().getWindow();
            curenta.setTitle("Test");
            curenta.setScene(new Scene(root, 900, 700));
    }
}
