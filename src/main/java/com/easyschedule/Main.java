package com.easyschedule;

import com.utils.JDBC;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        JDBC.openConnection();
        JDBC.closeConnection();
    }
}
