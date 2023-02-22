package com.utils;

import java.sql.*;

public abstract class Query {
    private static PreparedStatement statement;
    private static String sql;


    public static ResultSet selectAll(String values, String table) {
        sql = "SELECT " + values + " FROM " + table;
        try {
            statement = JDBC.connection.prepareStatement(sql);
            return statement.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
// Need to make this more like the insert statement.
    public static ResultSet selectConditional(String values, String table, String condition, String comparison) {
        sql = "SELECT " + values + " FROM " + table + " WHERE " + condition + " ?";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, comparison);
            return statement.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static boolean insert(String table, String columns, String ... values) {
        String bindVariables = "?";
        if (values.length > 1) {
            for (int i = 0; i < values.length - 1; i++) {
                bindVariables += ", ?";
            }
        }
        sql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + bindVariables + ")";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                try {
                    statement.setInt(i+1, Integer.parseInt(values[i]));
                    continue;
                } catch (NumberFormatException ignored){}
                try {
                    // Need to convert timestamp to UTC before updating DB!!!!!!
                    Timestamp timestamp = new Timestamp(Long.valueOf(values[i]));
                    statement.setTimestamp(i+1, timestamp);
                    continue;
                } catch (IllegalArgumentException ignored){}
                statement.setString(i+1, values[i]);
            }
            statement.execute();
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static boolean delete(String table, String condition, String comparison) {
        sql = "DELETE FROM " + table + " WHERE " + condition + " ?";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, comparison);
            statement.execute();
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }
}
