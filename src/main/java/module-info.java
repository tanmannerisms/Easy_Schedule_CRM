module com.easyschedule.wgu_c195 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.easyschedule to javafx.fxml;
    exports com.easyschedule;
}