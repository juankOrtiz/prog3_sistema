module com.juan.sistema {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.juan.sistema to javafx.fxml;
    exports com.juan.sistema;
}