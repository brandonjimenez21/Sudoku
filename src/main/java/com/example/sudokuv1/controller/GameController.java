package com.example.sudokuv1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import com.example.sudokuv1.model.SudokuModel;
import com.example.sudokuv1.view.GameView;

import java.util.HashSet;
import java.util.Set;

public class GameController {
    @FXML
    private GridPane sudokuGrid;

    @FXML
    private Label messageLabel;

    @FXML
    private Button validateBoardButton; // Referencia al botón de validar

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
    }

    public void validateNumber() {

        TextField lastEdited = view.getLastEditedCell();
        if (lastEdited != null && !lastEdited.getText().isEmpty()) {
            int number = Integer.parseInt(lastEdited.getText());
            // Buscar la posición de la celda editada
            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 6; col++) {
                    if (view.getCells()[row][col] == lastEdited) {
                        if (model.isValid(row, col, number)) {
                            lastEdited.setStyle("-fx-background-color: lightgreen;"); // Correcto
                        } else {
                            lastEdited.setStyle("-fx-background-color: lightcoral;"); // Incorrecto
                        }
                        return;
                    }
                }
            }
        }
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
                    view.getCells()[row][col].setStyle("-fx-background-color: lightcoral;");
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
                    view.getCells()[col][row].setStyle("-fx-background-color: lightcoral;");
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
                            view.getCells()[blockRow + row][blockCol + col].setStyle("-fx-background-color: lightcoral;");
                        } else {
                            view.getCells()[blockRow + row][blockCol + col].setStyle(""); // Restablecer si es válido
                        }
                    }
                }
            }
        }

        // Mostrar resultado de validación
        if (!isComplete) {
            messageLabel.setText("El tablero no está completo. Rellena todas las celdas antes de validar.");
        } else if (isValid) {
            messageLabel.setText("¡Felicidades! El tablero está completo y es válido. Has ganado.");
        } else {
            messageLabel.setText("Hay errores en el tablero:\n" + errorMessage);
        }
    }
}
