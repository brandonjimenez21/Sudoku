package com.example.sudokuv1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import com.example.sudokuv1.model.SudokuModel;
import com.example.sudokuv1.view.GameView;

import java.util.HashSet;
import java.util.Set;

public class GameController {
    @FXML
    private GridPane sudokuGrid;

    @FXML
    private Button validateBoardButton; // Referencia al botón de validar
    @FXML
    private Button retryButton; // Referencia al botón de intentar de nuevo
    @FXML
    private Button newGameButton; // Referencia al botón de nuevo juego

    private SudokuModel model;
    private GameView view;

    public GameController() {
        model = new SudokuModel();
    }

    @FXML
    public void initialize() {
        view = new GameView(sudokuGrid, model, validateBoardButton); // Pasar el botón al GameView
        view.renderGrid();
        validateBoardButton.setDisable(true); // Deshabilitar el botón al inicio
        retryButton.setDisable(true); // Deshabilitar el botón de intentar de nuevo al inicio
        newGameButton.setDisable(true); // Deshabilitar el botón de nuevo juego al inicio
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
                String cellText = view.getCells()[row][col].getText();
                if (cellText.isEmpty()) {
                    isComplete = false;
                    continue;
                }

                int number = Integer.parseInt(cellText);
                if (!rowCheck.add(number)) {
                    isValid = false;
                    errorMessage.append("Número repetido en fila ").append(row + 1)
                            .append(", columna ").append(col + 1).append(".\n");
                    view.getCells()[row][col].setStyle("-fx-background-color: lightcoral; -fx-alignment: center;");
                } else {
                    view.getCells()[row][col].setStyle(""); // Restablecer si es válido
                }

                // Verificar columna
                String colCellText = view.getCells()[col][row].getText();
                if (colCellText.isEmpty()) {
                    isComplete = false;
                    continue;
                }

                int colNumber = Integer.parseInt(colCellText);
                if (!colCheck.add(colNumber)) {
                    isValid = false;
                    errorMessage.append("Número repetido en columna ").append(row + 1)
                            .append(", fila ").append(col + 1).append(".\n");
                    view.getCells()[col][row].setStyle("-fx-background-color: lightcoral; -fx-alignment: center;");
                } else {
                    view.getCells()[col][row].setStyle(""); // Restablecer si es válido
                }
            }
        }

        // Validar bloques de 2x3
        for (int blockRow = 0; blockRow < 6; blockRow += 2) {
            for (int blockCol = 0; blockCol < 6; blockCol += 3) {
                Set<Integer> blockCheck = new HashSet<>();
                for (int row = 0; row < 2; row++) {
                    for (int col = 0; col < 3; col++) {
                        String cellText = view.getCells()[blockRow + row][blockCol + col].getText();
                        if (cellText.isEmpty()) {
                            isComplete = false;
                            continue;
                        }

                        int number = Integer.parseInt(cellText);
                        if (!blockCheck.add(number)) {
                            isValid = false;
                            errorMessage.append("Número repetido en el bloque comenzando en fila ")
                                    .append(blockRow + 1)
                                    .append(" y columna ")
                                    .append(blockCol + 1).append(".\n");
                            view.getCells()[blockRow + row][blockCol + col].setStyle("-fx-background-color: lightcoral; -fx-alignment: center;");
                        } else {
                            view.getCells()[blockRow + row][blockCol + col].setStyle(""); // Restablecer si es válido
                        }
                    }
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
            retryButton.setDisable(true); // Deshabilitar botón de intentar de nuevo
            newGameButton.setDisable(false); // Habilitar botón de nuevo juego
        } else {
            showAlert("Errores en el tablero", "Error", "Hay errores en el tablero:\n" + errorMessage);
            retryButton.setDisable(false); // Habilitar botón de intentar de nuevo
            newGameButton.setDisable(true); // Deshabilitar botón de nuevo juego
        }
    }

    // Método para intentar de nuevo
    @FXML
    public void retry() {
        view.clearCellStyles(); // Restablecer estilos de las celdas
        view.renderGrid(); // Volver a renderizar el tablero
        validateBoardButton.setDisable(true); // Deshabilitar el botón de validar
        retryButton.setDisable(true); // Deshabilitar botón de intentar de nuevo
        newGameButton.setDisable(true); // Deshabilitar botón de nuevo juego
    }

    // Método para nuevo juego
    @FXML
    public void newGame() {
        model = new SudokuModel(); // Generar un nuevo modelo
        view = new GameView(sudokuGrid, model, validateBoardButton); // Crear nueva vista
        view.renderGrid(); // Renderizar nuevo tablero
        validateBoardButton.setDisable(true); // Deshabilitar el botón de validar
        retryButton.setDisable(true); // Deshabilitar botón de intentar de nuevo
        newGameButton.setDisable(true); // Deshabilitar botón de nuevo juego
    }

    // Método para mostrar Alertas
    private void showAlert(String title, String header, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}