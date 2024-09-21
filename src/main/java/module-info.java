module com.example.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires twilio;
    requires mysql.connector.j;

    opens com.example.passwordmanager to javafx.fxml;
    exports com.example.passwordmanager;
}