package com.example.sudokuv1.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WelcomeController {

    @FXML
    private Button startButton; // Botón del archivo FXML que dispara la acción

    @FXML
    public void startGame(ActionEvent event) throws Exception {
        // Obtener el Stage actual desde el botón (u otro nodo de la vista)
        Stage stage = (Stage) startButton.getScene().getWindow();

        // Cargar la nueva escena del juego
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sudokuv1/GameView.fxml"));
        Scene scene = new Scene(loader.load());

        // Cambiar la escena en el Stage actual
        stage.setScene(scene);
        stage.setTitle("Sudoku 6x6 - Juego");
    }
}