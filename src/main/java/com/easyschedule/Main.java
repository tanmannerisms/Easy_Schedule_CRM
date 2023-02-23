package com.easyschedule;

import com.utils.JDBC;
import com.utils.Query;
import javafx.application.Application;
import javafx.stage.Stage;
import com.window.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        JDBC.openConnection();

        Instance.updateAllLists();

//        test();

        Window login = new Window("login.fxml", "Login Page");
        login.showWindow();

    }
    @Override
    public void stop() {
        JDBC.closeConnection();
    }
    public void test() {
        Instant instant = Instant.now();
        System.out.println("Instant in UTC: " + instant + "\n\n");

        Timestamp timestamp = Timestamp.from(instant);
        Instant instant1 = timestamp.toInstant();

        System.out.println(instant1.toString());


        System.out.println(timestamp);

        ZoneId osZone = ZoneId.systemDefault();
        System.out.println("Zone ID of OS: " + osZone);



        ZonedDateTime zonedInstant = instant.atZone(osZone);
        System.out.println(zonedInstant);

        Instance.updateCustomer(Instance.getAllCustomers().get(1));
    }
}
