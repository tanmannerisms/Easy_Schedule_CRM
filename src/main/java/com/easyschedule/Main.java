package com.easyschedule;

import com.utils.JDBC;
import javafx.application.Application;
import javafx.stage.Stage;
import com.window.*;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        JDBC.openConnection();

        Window login = new Window("login.fxml", "Login Page");
        login.showWindow();

        JDBC.closeConnection();
    }
}
