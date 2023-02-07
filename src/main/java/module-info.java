module com.easyschedule.wgu_c195 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.easyschedule.wgu_c195 to javafx.fxml;
    exports com.easyschedule.wgu_c195;
}