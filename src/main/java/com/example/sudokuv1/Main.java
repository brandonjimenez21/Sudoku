package com.example.sudokuv1;

import javafx.application.Application;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sudokuv1/WelcomeView.fxml"));
        Scene scene = new Scene(loader.load());

        // Set the window icon
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/sudokuv1/images/icono.png"))));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Sudoku 6x6");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
