package io.github.manuelosorio.client;

import java.io.*;
import java.net.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ClientController {
    @FXML
    public TextArea resultArea;
    @FXML
    private TextField numberField;
    @FXML
    private Button submitButton;

    private static DataOutputStream dataToServerStream;

    public void initialize() {
//        this.resultArea.setStyle("-fx-font-family: monospace; -fx-font-weight: 500; -fx-font-size: 1.3em; -webkit-fx-background-c: #000; -fx-text-fill: #fff;");
        submitButton.setOnAction(event -> {
            try {
                Socket socket = new Socket("localhost", 8000);
                dataToServerStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream dataFromServerStream = new DataInputStream(socket.getInputStream());

                if (!numberField.getText().matches("\\d+|-1")) {
                    resultArea.appendText("Please enter a valid integer.\n");
                    return;
                }

                int n = Integer.parseInt(numberField.getText());

                dataToServerStream.writeInt(n);

                if (n == -1) {
                    if (dataFromServerStream.readInt() == -1) {
                        resultArea.appendText("Attempting to shutdown server.\n");
                        socket.close();
                        dataToServerStream.close();
                        dataFromServerStream.close();
                        CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(() -> {
                            resultArea.appendText("Server shutdown successfully.\n");
                            resultArea.appendText("Exiting client.\n");
                            CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
                                Platform.exit();
                                System.exit(0);
                            });
                        });
                        return;
                    }
                }

                boolean isPrime = dataFromServerStream.readBoolean();
                Platform.runLater(() -> resultArea.appendText(isPrime
                        ? n + " is prime.\n" : n + " is not prime.\n"));
                dataToServerStream.close();
                dataFromServerStream.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
