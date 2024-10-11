module com.example.sudokuv1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sudokuv1 to javafx.fxml;
    exports com.example.sudokuv1;
}