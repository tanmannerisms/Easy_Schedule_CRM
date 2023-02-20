module com.easyschedule.wgu_c195 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.easyschedule to javafx.fxml;
    exports com.easyschedule;
    exports com.window;
    opens com.window to javafx.fxml;
    exports com.controllers;
    opens com.controllers to javafx.fxml;
    exports com.people;
    opens com.people to javafx.fxml;
    exports com.location;
    opens com.location to javafx.fxml;
}