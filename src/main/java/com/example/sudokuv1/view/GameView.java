package com.example.sudokuv1.view;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import com.example.sudokuv1.model.SudokuModel;

public class GameView {
    private GridPane gridPane;
    private SudokuModel model;
    private TextField[][] cells; // Matriz para almacenar las celdas
    private TextField lastEditedCell; // Para rastrear la última celda editada
    private Button validateBoardButton; // Referencia al botón de validar

    public GameView(GridPane gridPane, SudokuModel model, Button validateBoardButton) {
        this.gridPane = gridPane;
        this.model = model;
        this.cells = new TextField[6][6]; // Inicializa la matriz de celdas
        this.validateBoardButton = validateBoardButton; // Inicializa el botón de validar
    }

    public void renderGrid() {
        gridPane.getChildren().clear(); // Limpia el GridPane
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                TextField cell = new TextField();
                cell.setPrefSize(50, 50);
                cell.setStyle("-fx-alignment: center; -fx-font-size: 18; -fx-border-color: #000000; -fx-border-width: 1;");
                int number = model.getGrid()[row][col];

                if (number != 0) {
                    cell.setText(Integer.toString(number)); // Establece el número como texto
                    cell.setEditable(false); // Deshabilita la edición de la celda
                    cell.setStyle("-fx-background-color: lightgray;"); // Cambia el color para mostrar que es fijo
                } else {
                    cell.setText(""); // Celdas vacías para ingresar números
                    cell.textProperty().addListener((observable, oldValue, newValue) -> {
                        // Validar que el nuevo valor sea un número entre 1 y 6
                        if (newValue.matches("^[1-6]?$")) {
                            // Mantener referencia a la última celda editada
                            lastEditedCell = cell; // Guardar la celda actualmente editada
                        } else {
                            cell.setText(oldValue); // Revertir si no es válido
                        }

                        // Deshabilitar el botón de validar si hay celdas vacías
                        checkBoardCompletion();
                    });
                }

                cells[row][col] = cell; // Guardar referencia en la matriz
                gridPane.add(cell, col, row); // Agregar el campo de texto al GridPane
            }
        }
    }

    public TextField[][] getCells() {
        return cells;
    }

    public void clearCellStyles() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                cells[row][col].setStyle("-fx-border-color: #000000;"); // Reiniciar estilo
            }
        }
    }

    // Método para comprobar si el tablero está completo
    public void checkBoardCompletion() {
        boolean isComplete = true;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    isComplete = false; // Si alguna celda está vacía, el tablero no está completo
                }
            }
        }
        // Habilitar o deshabilitar el botón de validar
        validateBoardButton.setDisable(!isComplete);
    }

    public TextField getLastEditedCell() {
        return lastEditedCell;
    }
}
