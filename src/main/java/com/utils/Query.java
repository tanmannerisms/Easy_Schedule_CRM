package com.utils;

import java.sql.*;
import java.time.Instant;

public abstract class Query {
    private static PreparedStatement statement;
    private static String sql;


    public static ResultSet selectAll(String columns, String table) {
        sql = "SELECT " + columns + " FROM " + table;
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
            setBindVariables(values);
            statement.execute();
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static boolean update(String table, String condition, String columns, String ... values) {
        String columnArr[] = new String[values.length];
        // Convert the columns String to an array of Strings that have the bind variable attached
        try {
            StringBuilder column = new StringBuilder();
            for (int i = 0, j = 0; i <= columns.length(); i++) {
                if (j >= values.length) {
                    throw new IndexOutOfBoundsException("Too many columns specified!\n" +
                            "Columns: " + j + " Values: " + (values.length - 1));
                }
                if (i == columns.length() || columns.charAt(i) == ',') {
                    if (i == columns.length()) {
                        column.append(" = ?");
                    }
                    else {
                        column.append(" = ?, ");
                    }
                    columnArr[j] = String.valueOf(column);
                    j++;
                    column = new StringBuilder();
                } else if (columns.charAt(i) == ' ') {
                    // Do nothing
                } else {
                    column.append(columns.charAt(i));
                }
            }
            if (columnArr[values.length - 1] == null) {
                throw new IndexOutOfBoundsException("Not enough columns specified for number of values given!");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Number of columns and number of values do not match!");
            System.out.println(e.getMessage());
        }
        columns = "";
        // Convert it back to a String for use in concatenation.
        for (String string : columnArr) {
            columns += string;
        }
        sql = "UPDATE " + table + " SET " + columns + " WHERE " + condition ;
        try {
            statement = JDBC.connection.prepareStatement(sql);
            setBindVariables(values);
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
    private static void setBindVariables(String values[]) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            try {
                statement.setInt(i+1, Integer.parseInt(values[i]));
                continue;
            } catch (NumberFormatException ignored){}
            try {
                Instant now = Instant.ofEpochMilli(Long.valueOf(values[i]));
                Timestamp timestamp = Timestamp.from(now);
                System.out.println(timestamp);
                statement.setTimestamp(i+1, timestamp);
                continue;
            } catch (IllegalArgumentException ignored){}
            statement.setString(i+1, values[i]);
        }
    }
}
