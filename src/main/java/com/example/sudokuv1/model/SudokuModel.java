package com.example.sudokuv1.model;

import java.util.Random;

public class SudokuModel {
    private int[][] grid;
    private Random random;

    public SudokuModel() {
        grid = new int[6][6];
        random = new Random();
        generateCompleteGrid();
        removeNumbers();
    }

    private void generateCompleteGrid() {
        fillGrid(0, 0);
    }

    private boolean fillGrid(int row, int col) {
        // Si llegamos al final de la fila, movernos a la siguiente
        if (row == 6) {
            return true;
        }
        // Si la columna es 6, mover a la siguiente fila
        if (col == 6) {
            return fillGrid(row + 1, 0);
        }

        // Solo intentamos llenar celdas vacías
        if (grid[row][col] != 0) {
            return fillGrid(row, col + 1);
        }

        // Probar números del 1 al 6
        for (int num = 1; num <= 6; num++) {
            if (isValid(row, col, num)) {
                grid[row][col] = num;

                // Recursión para llenar el siguiente espacio
                if (fillGrid(row, col + 1)) {
                    return true;
                }

                // Deshacer si no funciona
                grid[row][col] = 0;
            }
        }
        return false;
    }

    public boolean isValid(int row, int col, int number) {
        // Verificar fila
        for (int i = 0; i < 6; i++) {
            if (i != col && grid[row][i] == number) {
                return false;
            }
        }

        // Verificar columna
        for (int i = 0; i < 6; i++) {
            if (i != row && grid[i][col] == number) {
                return false;
            }
        }

        // Verificar bloque 3x2
        int blockRowStart = (row / 2) * 2;
        int blockColStart = (col / 3) * 3;

        for (int i = blockRowStart; i < blockRowStart + 2; i++) {
            for (int j = blockColStart; j < blockColStart + 3; j++) {
                if ((i != row || j != col) && grid[i][j] == number) {
                    return false;
                }
            }
        }

        return true;
    }

    private void removeNumbers() {
        int cellsToRemove = 18; // Ajusta la dificultad modificando este número
        while (cellsToRemove > 0) {
            int row = random.nextInt(6);
            int col = random.nextInt(6);
            if (grid[row][col] != 0) {
                grid[row][col] = 0; // Eliminar número
                cellsToRemove--;
            }
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setNumber(int row, int col, int number) {
        grid[row][col] = number;
    }
}