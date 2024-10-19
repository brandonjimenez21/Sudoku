package com.example.sudokuv1.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WelcomeController {

    @FXML
    private Button startButton; // FXML file button that triggers the action

    @FXML
    public void startGame(ActionEvent event) throws Exception {
        // Get the current scenario from the button (or other view node)
        Stage stage = (Stage) startButton.getScene().getWindow();

        // Load the new game scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sudokuv1/GameView.fxml"));
        Scene scene = new Scene(loader.load());

        // Change the scene in the current Stage
        stage.setScene(scene);
        stage.setTitle("Sudoku 6x6 - Juego");
    }
}