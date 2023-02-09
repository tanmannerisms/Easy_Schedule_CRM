package com.easyschedule;

import com.utilities.JDBC;
import javafx.application.Application;
import javafx.stage.Stage;
import com.window.*;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //JDBC.openConnection();
        //JDBC.closeConnection();
        Window login = new Window("login.fxml", "Login Page");
        login.showWindow();
    }
}
