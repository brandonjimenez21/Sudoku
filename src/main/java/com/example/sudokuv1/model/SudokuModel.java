package com.example.sudokuv1.model;

import java.util.ArrayList;
import java.util.Random;

public class SudokuModel {
    private ArrayList<ArrayList<Integer>> grid;
    private Random random;

    public SudokuModel() {
        grid = new ArrayList<>(6); // Initialize the ArrayList
        for (int i = 0; i < 6; i++) {
            grid.add(new ArrayList<>(6)); // Add an ArrayList for each row
            for (int j = 0; j < 6; j++) {
                grid.get(i).add(0); // Initialize with 0
            }
        }
        random = new Random();
        generateCompleteGrid();
        removeNumbers();
    }

    private void generateCompleteGrid() {
        fillGrid(0, 0);
    }

    private boolean fillGrid(int row, int col) {
        // If we reach the end of the row, move to the next
        if (row == 6) {
            return true;
        }
        // If column is 6, move to next row
        if (col == 6) {
            return fillGrid(row + 1, 0);
        }

        // We only try to fill empty cells
        if (grid.get(row).get(col) != 0) {
            return fillGrid(row, col + 1);
        }

        // Test numbers from 1 to 6
        for (int num = 1; num <= 6; num++) {
            if (isValid(row, col, num)) {
                grid.get(row).set(col, num); // Use set to set the number

                // Recursion to fill the next space
                if (fillGrid(row, col + 1)) {
                    return true;
                }

                // Undo if it doesn't work
                grid.get(row).set(col, 0); // Set back to 0
            }
        }
        return false;
    }

    public boolean isValid(int row, int col, int number) {
        // Check row
        for (int i = 0; i < 6; i++) {
            if (i != col && grid.get(row).get(i) == number) {
                return false;
            }
        }

        // Check column
        for (int i = 0; i < 6; i++) {
            if (i != row && grid.get(i).get(col) == number) {
                return false;
            }
        }

        // Check 2x3 block
        int blockRowStart = (row / 2) * 2;
        int blockColStart = (col / 3) * 3;

        for (int i = blockRowStart; i < blockRowStart + 2; i++) {
            for (int j = blockColStart; j < blockColStart + 3; j++) {
                if ((i != row || j != col) && grid.get(i).get(j) == number) {
                    return false;
                }
            }
        }

        return true;
    }

    private void removeNumbers() {
        // We go through the 2x3 blocks
        for (int blockRow = 0; blockRow < 6; blockRow += 2) {
            for (int blockCol = 0; blockCol < 6; blockCol += 3) {
                // Create a list with the positions within the 2x3 block
                ArrayList<int[]> positions = new ArrayList<>();
                for (int i = blockRow; i < blockRow + 2; i++) {
                    for (int j = blockCol; j < blockCol + 3; j++) {
                        positions.add(new int[]{i, j});
                    }
                }

                //Shuffle the positions to choose 4 randomly
                shufflePositions(positions);

                // Delete 4 of the 6 positions in the block (set them to 0)
                for (int k = 0; k < 4; k++) {
                    int[] pos = positions.get(k);
                    grid.get(pos[0]).set(pos[1], 0); // Set the cell to 0
                }
            }
        }
    }

    private void shufflePositions(ArrayList<int[]> positions) {
        Random random = new Random();
        for (int i = positions.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // Swap positions[i] with positions[j]
            int[] temp = positions.get(i);
            positions.set(i, positions.get(j));
            positions.set(j, temp);
        }
    }

    public ArrayList<ArrayList<Integer>> getGrid() {
        return grid;
    }

    public void setNumber(int row, int col, int number) {
        grid.get(row).set(col, number); // Use set to set the number
    }
}
