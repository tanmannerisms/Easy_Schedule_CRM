module com.easyschedule.wgu_c195 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.easyschedule.wgu_c195 to javafx.fxml;
    exports com.easyschedule.wgu_c195;
}