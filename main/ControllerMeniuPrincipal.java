package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

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

        }else if(f.getName().contains(".p7b") || f.getName().contains(".p7s") || f.getName().contains(".p7m")){
            fisierSemnat = f;
            //extragerea payloadului din mesajul semnat
            try {
                FileInputStream in = new FileInputStream(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            butonValidareSemnatura.setVisible(true);
        }
        labelFisier.setText(f.getName());

    }

    @FXML
    private void openCitireXML(){

        if(ControllerMeniuPrincipal.fisierXML == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oroare!!!");
            alert.setHeaderText("Nu a fost incarcat un fisier!");
            alert.setContentText("Pentru Citirea XML-ului trebuie incarcat un fisier în format .xml");
            alert.showAndWait();
            return;
        }

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

    @FXML
    private void openValidareSemnatura(){
        //showMessageDialog(null,"Test");
        System.out.println("Modul de validare semnatura");
    }
}
