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

    private static ResultSet prepareAndExecute() {
        try {
            statement = JDBC.connection.prepareStatement(sql);
            return statement.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ResultSet selectAll(String values, String table) {
        sql = "SELECT " + values + "FROM " + table;
        return prepareAndExecute();
    }

    public static ResultSet selectConditional(String values, String table, String conditions) {
        sql = "SELECT " + values + " FROM " + table + " WHERE " + conditions;
        return prepareAndExecute();
    }
    public static boolean delete(String table, String condition) {
        sql = "DELETE FROM " + table + " WHERE " + condition;
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.execute();
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }
    public static User getUser(String username) {
        User newUser = new User();
        sql = "SELECT User_Id, User_Name, Password FROM users WHERE User_Name = ?";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                newUser = new User(resultSet.getInt("User_Id"),
                                   resultSet.getString("User_Name"),
                                   resultSet.getString("Password"));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return newUser;
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
