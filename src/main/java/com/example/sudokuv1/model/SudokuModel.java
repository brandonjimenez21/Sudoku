package com.example.sudokuv1.model;

import java.util.ArrayList;
import java.util.Random;

public class SudokuModel {
    private ArrayList<ArrayList<Integer>> grid; // Usar ArrayList en lugar de matriz
    private Random random;

    public SudokuModel() {
        grid = new ArrayList<>(6); // Inicializa el ArrayList
        for (int i = 0; i < 6; i++) {
            grid.add(new ArrayList<>(6)); // Añadir un ArrayList por cada fila
            for (int j = 0; j < 6; j++) {
                grid.get(i).add(0); // Inicializar con 0
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
        // Si llegamos al final de la fila, movernos a la siguiente
        if (row == 6) {
            return true;
        }
        // Si la columna es 6, mover a la siguiente fila
        if (col == 6) {
            return fillGrid(row + 1, 0);
        }

        // Solo intentamos llenar celdas vacías
        if (grid.get(row).get(col) != 0) {
            return fillGrid(row, col + 1);
        }

        // Probar números del 1 al 6
        for (int num = 1; num <= 6; num++) {
            if (isValid(row, col, num)) {
                grid.get(row).set(col, num); // Usar set para establecer el número

                // Recursión para llenar el siguiente espacio
                if (fillGrid(row, col + 1)) {
                    return true;
                }

                // Deshacer si no funciona
                grid.get(row).set(col, 0); // Volver a establecer en 0
            }
        }
        return false;
    }

    public boolean isValid(int row, int col, int number) {
        // Verificar fila
        for (int i = 0; i < 6; i++) {
            if (i != col && grid.get(row).get(i) == number) {
                return false;
            }
        }

        // Verificar columna
        for (int i = 0; i < 6; i++) {
            if (i != row && grid.get(i).get(col) == number) {
                return false;
            }
        }

        // Verificar bloque 2x3
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
        // Recorremos los bloques de 2x3
        for (int blockRow = 0; blockRow < 6; blockRow += 2) {
            for (int blockCol = 0; blockCol < 6; blockCol += 3) {
                // Crear una lista con las posiciones dentro del bloque 2x3
                ArrayList<int[]> positions = new ArrayList<>();
                for (int i = blockRow; i < blockRow + 2; i++) {
                    for (int j = blockCol; j < blockCol + 3; j++) {
                        positions.add(new int[]{i, j});
                    }
                }

                // Barajar las posiciones para elegir 4 aleatoriamente
                shufflePositions(positions);

                // Eliminar 4 de las 6 posiciones en el bloque (ponerlas en 0)
                for (int k = 0; k < 4; k++) {
                    int[] pos = positions.get(k);
                    grid.get(pos[0]).set(pos[1], 0); // Establecer la celda en 0
                }
            }
        }
    }

    private void shufflePositions(ArrayList<int[]> positions) {
        Random random = new Random();
        for (int i = positions.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // Intercambiar posiciones[i] con posiciones[j]
            int[] temp = positions.get(i);
            positions.set(i, positions.get(j));
            positions.set(j, temp);
        }
    }

    public ArrayList<ArrayList<Integer>> getGrid() {
        return grid;
    }

    public void setNumber(int row, int col, int number) {
        grid.get(row).set(col, number); // Usar set para establecer el número
    }
}
