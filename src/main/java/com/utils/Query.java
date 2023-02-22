package com.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
