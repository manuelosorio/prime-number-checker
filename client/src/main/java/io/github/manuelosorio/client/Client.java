package io.github.manuelosorio.client;

import java.io.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("client.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, 300, 250);
        primaryStage.setTitle("Prime Number Checker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}