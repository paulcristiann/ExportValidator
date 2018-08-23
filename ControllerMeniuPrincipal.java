package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import static javax.swing.JOptionPane.showMessageDialog;

public class ControllerMeniuPrincipal {

    public File fisierXML;
    public File fisierSemnat;
    public Button butonCitireXML;
    public Label labelFisier;

    @FXML
    private void incarcaFisier(){

        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showOpenDialog(null);
        if (f != null) {
            if(identifyFileTypeUsingFilesProbeContentType(fisierSemnat).equals("application/x-pkcs7-certificates")){

                System.out.println("Trenuie extras XML-ul");
                fisierSemnat = f;
            }
            else if(identifyFileTypeUsingFilesProbeContentType(fisierSemnat).equals("text/xml")){
                fisierXML = f;
                //trebuie sa suprimam butonul de verificare certificat
            }
            else {
                showMessageDialog(null, "Fisier incompatibil!");
            }
            showMessageDialog(null, "Fisier incarcat!");
        }
        else {
            System.out.println("Alegere anulata");
        }

    }

    public String identifyFileTypeUsingFilesProbeContentType(final File fileName)
    {
        String fileType = "Undetermined";
        final File file = fileName;
        try
        {
            fileType = Files.probeContentType(file.toPath());
        }
        catch (IOException ioException)
        {
            System.out.println("Nu putem determina tipul fisierului");
        }
        return fileType;
    }

    @FXML
    private void openCitireXML(){

        ReadXMLFile obiectReadXML = new ReadXMLFile();
        obiectReadXML.setFisierXML(fisierXML);
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
