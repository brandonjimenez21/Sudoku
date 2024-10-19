package com.example.sudokuv1.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WelcomeView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sudokuv1/GameView.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Bienvenido a Sudoku");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }
}