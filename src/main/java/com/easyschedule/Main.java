package com.easyschedule;

import com.utils.JDBC;
import com.window.Window;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        JDBC.openConnection();

        Instance.updateAllLists();


        Window login = new Window("login.fxml", "Login Page");
        login.showWindow();

    }
    @Override
    public void stop() {
        JDBC.closeConnection();
    }
    public void test() {
//        URL url = getClass().getResource("login.fxml");
//        System.out.println(url);
//        SecureClassLoader loader = new URLClassLoader(url);
        Module module = getClass().getModule();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "com.easyschedule.lang.Nat",
                Locale.getDefault()
        );
    }
}
