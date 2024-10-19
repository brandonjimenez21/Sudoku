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
    private ArrayList<ArrayList<TextField>> cells; // Switch to ArrayList to store the cells
    private TextField lastEditedCell; // To track the last edited cell
    private Button validateBoardButton; // Reference to the validate button

    public GameView(GridPane gridPane, SudokuModel model, Button validateBoardButton) {
        this.gridPane = gridPane;
        this.model = model;
        this.cells = new ArrayList<>(); // Initialize the ArrayList
        this.validateBoardButton = validateBoardButton; // Initialize the validate button
        initializeCells(); // Initialize the cells
    }

    public void initializeCells() {
        for (int row = 0; row < 6; row++) {
            ArrayList<TextField> cellRow = new ArrayList<>(); // Create a row of cells
            for (int col = 0; col < 6; col++) {
                TextField cell = new TextField();
                cell.setPrefSize(50, 50);
                cell.setStyle("-fx-alignment: center; -fx-font-size: 18; -fx-border-color: #000000; -fx-border-width: 1;");
                int number = model.getGrid().get(row).get(col);

                if (number != 0) {
                    cell.setText(Integer.toString(number)); // Set the number as text
                    cell.setEditable(false); // Disables cell editing
                    cell.setStyle("-fx-background-color: lightgray; -fx-alignment: center;"); // Change the color to show that it is fixed
                } else {
                    cell.setText(""); // Empty cells to enter numbers
                    int finalRow = row;
                    int finalCol = col;
                    cell.textProperty().addListener((observable, oldValue, newValue) -> {
                        // Validate that the new value is a number between 1 and 6
                        if (newValue.matches("^[1-6]?$")) {
                            // Keep reference to the last edited cell
                            lastEditedCell = cell; // Save the currently edited cell

                            // Check the validity of the number in the row, column and block
                            validateCell(finalRow, finalCol, newValue);
                        } else {
                            cell.setText(oldValue); // Rollback if invalid
                        }

                        // Disable the validate button if there are empty cells
                        checkBoardCompletion();
                    });
                }

                cellRow.add(cell); // Save the cell to the row
                gridPane.add(cell, col, row); // Add the text field to the GridPane
            }
            cells.add(cellRow); // Add the row of cells to the list of cells
        }
    }

    private void validateCell(int row, int col, String value) {
        // Check if the value is empty
        if (value.isEmpty()) {
            lastEditedCell.setStyle(""); // Reset style if no value
            return; // Exit if there is no number to validate
        }

        int number;
        try {
            number = Integer.parseInt(value); // convert a number
        } catch (NumberFormatException e) {
            lastEditedCell.setStyle("-fx-background-color: lightcoral;"); // Style for invalid
            return; // Exit if cannot be converted
        }

        boolean isValid = true;

        // Validate in row
        for (int c = 0; c < 6; c++) {
            if (c != col && cells.get(row).get(c).getText().equals(value)) {
                isValid = false; // The number already exists in the row
                break;
            }
        }

        // Validate on column
        for (int r = 0; r < 6; r++) {
            if (r != row && cells.get(r).get(col).getText().equals(value)) {
                isValid = false; // The number already exists in the column
                break;
            }
        }

        // Validate in the 2x3 block
        int startRow = (row / 2) * 2; // Start the block of 2 rows
        int startCol = (col / 3) * 3; // Start the 3 column block
        for (int r = startRow; r < startRow + 2; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if ((r != row || c != col) && cells.get(r).get(c).getText().equals(value)) {
                    isValid = false; // The number already exists in the 2x3 block
                    break;
                }
            }
        }

        // Apply style based on validity
        if (isValid) {
            lastEditedCell.setStyle("-fx-background-color: lightgreen; -fx-alignment: center;"); // Style for valid
        } else {
            lastEditedCell.setStyle("-fx-background-color: lightcoral; -fx-alignment: center;"); // Style for invalid
        }
    }

    public ArrayList<ArrayList<TextField>> getCells() {
        return cells; // Return the list of cells
    }

    public void clearCellStyles() {
        for (ArrayList<TextField> cellRow : cells) {
            for (TextField cell : cellRow) {
                cell.setStyle("-fx-border-color: #000000;"); // Reset style
            }
        }
    }

    // Method to check if the board is complete
    public void checkBoardCompletion() {
        boolean isComplete = true;
        for (ArrayList<TextField> cellRow : cells) {
            for (TextField cell : cellRow) {
                if (cell.getText().isEmpty()) {
                    isComplete = false; // If any cell is empty, the board is not complete
                }
            }
        }
        // Enable or disable the validate button
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
