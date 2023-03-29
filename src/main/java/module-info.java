module com.easyschedule.wgu_c195 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires junit;
    requires jfx.testrunner;
    requires org.junit.jupiter.api;


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
    exports com.testing to junit;
}