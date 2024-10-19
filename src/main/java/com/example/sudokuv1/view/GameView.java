package com.example.sudokuv1.view;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import com.example.sudokuv1.model.SudokuModel;

import java.util.ArrayList;

public class GameView {
    private GridPane gridPane;
    private SudokuModel model;
    private ArrayList<ArrayList<TextField>> cells; // Cambia a ArrayList para almacenar las celdas
    private TextField lastEditedCell; // Para rastrear la última celda editada
    private Button validateBoardButton; // Referencia al botón de validar

    public GameView(GridPane gridPane, SudokuModel model, Button validateBoardButton) {
        this.gridPane = gridPane;
        this.model = model;
        this.cells = new ArrayList<>(); // Inicializa el ArrayList
        this.validateBoardButton = validateBoardButton; // Inicializa el botón de validar
        initializeCells(); // Inicializa las celdas
    }

    public void initializeCells() {
        for (int row = 0; row < 6; row++) {
            ArrayList<TextField> cellRow = new ArrayList<>(); // Crea una fila de celdas
            for (int col = 0; col < 6; col++) {
                TextField cell = new TextField();
                cell.setPrefSize(50, 50);
                cell.setStyle("-fx-alignment: center; -fx-font-size: 18; -fx-border-color: #000000; -fx-border-width: 1;");
                int number = model.getGrid().get(row).get(col);

                if (number != 0) {
                    cell.setText(Integer.toString(number)); // Establece el número como texto
                    cell.setEditable(false); // Deshabilita la edición de la celda
                    cell.setStyle("-fx-background-color: lightgray; -fx-alignment: center;"); // Cambia el color para mostrar que es fijo
                } else {
                    cell.setText(""); // Celdas vacías para ingresar números
                    int finalRow = row;
                    int finalCol = col;
                    cell.textProperty().addListener((observable, oldValue, newValue) -> {
                        // Validar que el nuevo valor sea un número entre 1 y 6
                        if (newValue.matches("^[1-6]?$")) {
                            // Mantener referencia a la última celda editada
                            lastEditedCell = cell; // Guardar la celda actualmente editada

                            // Comprobar la validez del número en la fila, columna y bloque
                            validateCell(finalRow, finalCol, newValue);
                        } else {
                            cell.setText(oldValue); // Revertir si no es válido
                        }

                        // Deshabilitar el botón de validar si hay celdas vacías
                        checkBoardCompletion();
                    });
                }

                cellRow.add(cell); // Guarda la celda en la fila
                gridPane.add(cell, col, row); // Agrega el campo de texto al GridPane
            }
            cells.add(cellRow); // Añade la fila de celdas a la lista de celdas
        }
    }

    private void validateCell(int row, int col, String value) {
        // Verificar si el valor es vacío
        if (value.isEmpty()) {
            lastEditedCell.setStyle(""); // Restablecer estilo si no hay valor
            return; // Salir si no hay número para validar
        }

        int number;
        try {
            number = Integer.parseInt(value); // Convertir a número
        } catch (NumberFormatException e) {
            lastEditedCell.setStyle("-fx-background-color: lightcoral;"); // Estilo para inválido
            return; // Salir si no se puede convertir
        }

        boolean isValid = true;

        // Validar en la fila
        for (int c = 0; c < 6; c++) {
            if (c != col && cells.get(row).get(c).getText().equals(value)) {
                isValid = false; // El número ya existe en la fila
                break;
            }
        }

        // Validar en la columna
        for (int r = 0; r < 6; r++) {
            if (r != row && cells.get(r).get(col).getText().equals(value)) {
                isValid = false; // El número ya existe en la columna
                break;
            }
        }

        // Validar en el bloque de 2x3
        int startRow = (row / 2) * 2; // Inicia el bloque de 2 filas
        int startCol = (col / 3) * 3; // Inicia el bloque de 3 columnas
        for (int r = startRow; r < startRow + 2; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if ((r != row || c != col) && cells.get(r).get(c).getText().equals(value)) {
                    isValid = false; // El número ya existe en el bloque 2x3
                    break;
                }
            }
        }

        // Aplicar estilo según la validez
        if (isValid) {
            lastEditedCell.setStyle("-fx-background-color: lightgreen; -fx-alignment: center;"); // Estilo para válido
        } else {
            lastEditedCell.setStyle("-fx-background-color: lightcoral; -fx-alignment: center;"); // Estilo para inválido
        }
    }

    public ArrayList<ArrayList<TextField>> getCells() {
        return cells; // Retornar la lista de celdas
    }

    public void clearCellStyles() {
        for (ArrayList<TextField> cellRow : cells) {
            for (TextField cell : cellRow) {
                cell.setStyle("-fx-border-color: #000000;"); // Reiniciar estilo
            }
        }
    }

    // Método para comprobar si el tablero está completo
    public void checkBoardCompletion() {
        boolean isComplete = true;
        for (ArrayList<TextField> cellRow : cells) {
            for (TextField cell : cellRow) {
                if (cell.getText().isEmpty()) {
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

    public void showAlert(String title, String header, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
