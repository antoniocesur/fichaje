module com.example.fichaje {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;

    opens com.example.fichaje to javafx.fxml;
    exports com.example.fichaje;
}