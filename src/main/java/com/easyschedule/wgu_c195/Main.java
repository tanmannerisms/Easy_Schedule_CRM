package com.easyschedule.wgu_c195;

import com.utilities.wgu_c195.JDBC;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        JDBC.openConnection();
        JDBC.closeConnection();
    }
}
