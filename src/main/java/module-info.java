module com.example.sudokuv1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.sudokuv1.view to javafx.fxml;
    opens com.example.sudokuv1.controller to javafx.fxml;

    exports com.example.sudokuv1;
}