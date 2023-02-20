package com.utils;

import com.easyschedule.Appointment;
import com.easyschedule.Instance;
import com.people.Contact;
import com.people.Customer;
import com.people.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Query {
    private static PreparedStatement statement;
    private static String sql;


    public static ResultSet selectAll(String values, String table) {
        sql = "SELECT " + values + "FROM " + table;
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, values);
            statement.setString(2, table);
            return statement.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ResultSet selectConditional(String values, String table, String conditions) {
        sql = "SELECT ? FROM ? WHERE ?";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, values);
            statement.setString(2, table);
            statement.setString(3, conditions);
            return statement.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static boolean delete(String table, String condition) {
        sql = "DELETE FROM ? WHERE ?";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, table);
            statement.setString(2, condition);
            statement.execute();
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }
    public static String getDivision(int divisionId) {
        sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setInt(1, divisionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("Division");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
