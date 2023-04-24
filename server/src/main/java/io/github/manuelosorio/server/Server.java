package io.github.manuelosorio.server;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.github.manuelosorio.PrimeChecker;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Server extends Application {


    ServerController controller;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Server.fxml"));
        Parent parent = fxmlLoader.load();
        primaryStage.setTitle("Prime Number Checker - Server");
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
        this.controller = fxmlLoader.getController();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        Thread thread = new Thread(() -> {
            PrimeChecker checker = new PrimeChecker();
            boolean isRunning = true;
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                this.appendToLog("Server started at " + new Date());
                this.updateServerStatus("Server started.", "green");
                while(isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    appendToLog("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                    InputStream dataFromClientStream = clientSocket.getInputStream();
                    DataInputStream clientDataInput = new DataInputStream(dataFromClientStream);
                    OutputStream dataToClientStream = clientSocket.getOutputStream();
                    DataOutputStream clientDataOutput = new DataOutputStream(dataToClientStream);
                    int n = clientDataInput.readInt();
                    appendToLog("Client sent: " + n + " to server.");
                    if (n == -1) {
                        appendToLog("Client sent termination signal. Stopping server...");
                        this.updateServerStatus("Server stopped.", "red");
                        clientDataOutput.writeInt(-1);
                        clientDataInput.close();
                        clientDataOutput.close();
                        clientSocket.close();
                        serverSocket.close();
                        isRunning = false;
                        System.out.println("Server stopped.");
                        CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(() -> {
                            Platform.runLater(() -> this.appendToLog("Server stopped."));
                            CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> Platform.runLater(primaryStage::close));
                        });
                        continue;
                    }
                    boolean isPrime = checker.isPrime(n);
                    clientDataOutput.writeBoolean(isPrime);
                    clientDataInput.close();
                    clientDataOutput.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                this.appendToLog("Server error: " + e.getMessage());
                this.updateServerStatus("Server error.", "red");
            }
        });
        thread.start();
    }

    private void appendToLog(String s) {
        Platform.runLater(() -> this.controller.appendToLog(s));
    }
    private void updateServerStatus(String s, String color) {
        Platform.runLater(() -> this.controller.updateServerStatus(s, color));
    }
}