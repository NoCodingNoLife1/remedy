module com.mlms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.mlms to javafx.fxml;
    opens com.mlms.controller to javafx.fxml;
    opens com.mlms.model to javafx.base;
    
    exports com.mlms;
    exports com.mlms.controller;
    exports com.mlms.model;
}
