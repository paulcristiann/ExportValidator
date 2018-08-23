package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        ControllerMeniuPrincipal meniu = new ControllerMeniuPrincipal();

        Parent root = FXMLLoader.load(getClass().getResource("Meniu Principal.fxml"));
        primaryStage.setTitle("Validare rapoarte ANAF");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
