package io.github.manuelosorio.server;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class ServerController {

    @FXML
    private Label serverStatusLabel;


    @FXML
    private TextArea logArea;

    @FXML
    private ScrollPane scrollPane;
    private SimpleStringProperty serverStatus;

    public void initialize() {
        this.logArea.setEditable(false);
        this.logArea.setWrapText(true);
        this.scrollPane.vvalueProperty().bind(this.logArea.heightProperty());
        this.serverStatus = new SimpleStringProperty("Server is running");
        this.serverStatusLabel.textProperty().bind(this.serverStatus);
    }

    public void appendToLog(String message) {
        Platform.runLater(() -> {
            this.logArea.appendText(message + "\n");
        });
    }
    public void updateServerStatus(String status, String color) {
        Platform.runLater(() -> {
            this.serverStatus.setValue(status);
            this.serverStatusLabel.setStyle("-fx-text-fill: " + color);
        });
    }
}
