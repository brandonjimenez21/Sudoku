package com.example.sudokuv1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import com.example.sudokuv1.model.SudokuModel;
import com.example.sudokuv1.view.GameView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class GameController {
    @FXML
    private GridPane sudokuGrid;

    @FXML
    private Button validateBoardButton;
    @FXML
    private Button retryButton;
    @FXML
    private Button newGameButton;
    @FXML
    private Button helpButton;
    private int helpCount = 0;

    private SudokuModel model;
    private GameView view;

    public GameController() {
        model = new SudokuModel();
    }

    @FXML
    public void initialize() {
        view = new GameView(sudokuGrid, model, validateBoardButton);
        view.getCells();
        updateButtonStates();
    }

    private void updateButtonStates() {
        validateBoardButton.setDisable(true);
        retryButton.setDisable(true);
        newGameButton.setDisable(false);
        helpButton.setDisable(false);
    }

    public void validateBoard() {
        boolean isComplete = true;
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder("Errores encontrados:\n");

        // Validar filas y columnas
        for (int row = 0; row < 6; row++) {
            Set<Integer> rowCheck = new HashSet<>();
            Set<Integer> colCheck = new HashSet<>();

            for (int col = 0; col < 6; col++) {
                // Verificar fila
                String cellText = view.getCells().get(row).get(col).getText();
                if (cellText.isEmpty()) {
                    isComplete = false;  // Hay celdas vacías, el tablero no está completo
                    continue;  // No validamos números en celdas vacías
                }

                int number = Integer.parseInt(cellText);
                if (!rowCheck.add(number)) {
                    isValid = false;
                    errorMessage.append("Número repetido en fila ").append(row + 1)
                            .append(", columna ").append(col + 1).append(".\n");
                    view.getCells().get(row).get(col).setStyle("-fx-background-color: lightcoral; -fx-alignment: center;");
                }

                // Verificar columna
                String colCellText = view.getCells().get(col).get(row).getText();
                if (colCellText.isEmpty()) {
                    isComplete = false;  // Hay celdas vacías, el tablero no está completo
                    continue;  // No validamos números en celdas vacías
                }

                int colNumber = Integer.parseInt(colCellText);
                if (!colCheck.add(colNumber)) {
                    isValid = false;
                    errorMessage.append("Número repetido en columna ").append(col + 1)
                            .append(", fila ").append(row + 1).append(".\n");
                    view.getCells().get(col).get(row).setStyle("-fx-background-color: lightcoral; -fx-alignment: center;");
                }
            }
        }

        // Mostrar resultado de validación con Alert
        if (!isComplete) {
            showAlert("Tablero incompleto", "Error", "El tablero no está completo. Rellena todas las celdas antes de validar.");
            retryButton.setDisable(false); // Habilitar botón de intentar de nuevo
            newGameButton.setDisable(true); // Deshabilitar botón de nuevo juego
        } else if (isValid) {
            showAlert("¡Felicidades!", null, "¡El tablero está completo y es válido. Has ganado!");

            // Deshabilita la edicion de todas las celdas
            for (ArrayList<TextField> cellRow : view.getCells()){
                for (TextField cell : cellRow){
                    cell.setEditable(false); // Deshabilita la edicion
                }
            }
            retryButton.setDisable(true); // Deshabilitar botón de intentar de nuevo
            newGameButton.setDisable(false); // Habilitar botón de nuevo juego
        } else {
            showAlert("Errores en el tablero", "Error", "Hay errores en el tablero:\n" + errorMessage);
            retryButton.setDisable(false); // Habilitar botón de intentar de nuevo
            newGameButton.setDisable(true); // Deshabilitar botón de nuevo juego
        }
    }

    @FXML
    public void giveHelp() {
        if (helpCount >= 3) {
            showAlert("Limite alcanzado", "Error", "Has alcanzado el limite de ayudas disponible");
            helpButton.setDisable(true);
            return;
        }

        Random random = new Random();
        Set<Integer> emptyCells = new HashSet<>();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                if (view.getCells().get(row).get(col).getText().isEmpty()) {
                    emptyCells.add(row * 6 + col);
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            int cellPosition = (int) emptyCells.toArray()[random.nextInt(emptyCells.size())];
            int row = cellPosition / 6;
            int col = cellPosition % 6;

            // Sugerir un número que no esté en la fila, columna o bloque
            int suggestedNumber = suggestNumber(row, col);
            view.getCells().get(row).get(col).setText(Integer.toString(suggestedNumber));
            view.getCells().get(row).get(col).setStyle("-fx-background-color: lightblue; -fx-alignment: Center"); // Indicar ayuda

            helpCount++;
        }
    }

    private int suggestNumber(int row, int col) {
        boolean[] used = new boolean[7]; // Para números 1-6
        for (int i = 0; i < 6; i++) {
            // Marcar números en la fila
            String rowText = view.getCells().get(row).get(i).getText();
            if (!rowText.isEmpty()) {
                used[Integer.parseInt(rowText)] = true;
            }
            // Marcar números en la columna
            String colText = view.getCells().get(i).get(col).getText();
            if (!colText.isEmpty()) {
                used[Integer.parseInt(colText)] = true;
            }
        }
        // Sugerir un número no usado
        for (int i = 1; i <= 6; i++) {
            if (!used[i]) return i; // Retornar el primer número no usado
        }
        return -1; // Debería ser imposible
    }

    @FXML
    public void retry() {
        updateButtonStates();
        retryButton.setDisable(true);
    }

    @FXML
    public void newGame() {
        model = new SudokuModel();
        view = new GameView(sudokuGrid, model, validateBoardButton);
        view.getCells();
        updateButtonStates();
        helpCount = 0;
    }

    private void showAlert(String title, String header, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}