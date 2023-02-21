package com.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Query {
    private static PreparedStatement statement;
    private static String sql;


    public static ResultSet selectAll(String values, String table) {
        sql = "SELECT ? FROM ?";
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
}
