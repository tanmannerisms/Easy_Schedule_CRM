package com.easyschedule;

import com.utils.JDBC;
import com.utils.Query;
import javafx.application.Application;
import javafx.stage.Stage;
import com.window.*;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        JDBC.openConnection();

        Instance.updateAllLists();

        test();

        Window login = new Window("login.fxml", "Login Page");
        login.showWindow();

    }
    @Override
    public void stop() {
        JDBC.closeConnection();
    }
    public void test() {
        long now = System.currentTimeMillis();
        Query.update(
                "customers",
                "Customer_Id = " + 1,
                "Customer_Name, Phone, Address, Postal_Code, Division_Id, Last_Update, Last_Updated_By",
                "Tanner Mills",
                "541-571-6542",
                "1109 S. Leadville Ave",
                "83706 ",
                "1",
                String.valueOf(now),
                "Me"
        );
    }
}
