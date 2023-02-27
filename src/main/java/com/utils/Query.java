package com.utils;

import java.sql.*;
import java.time.Instant;

public abstract class Query {
    private static PreparedStatement statement;
    private static String sql;

    /**
     * Selects all rows from the specified table
     * @param columns the columns to select from the specified table.
     * @param table the table to query
     * @return the result set of the completed query.
     */
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

    /**
     * Selects unique values from a specific column in a table.
     * @param value the column that you want to select the unique value from.
     * @param table the table to query.
     * @return the result set of the completed query.
     */
    public static ResultSet selectUnique(String value, String table) {
        sql = "SELECT DISTINCT " + value + " FROM " + table;
        try {
            statement = JDBC.connection.prepareStatement(sql);
            return statement.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Select rows from a table that match the comparison value specified.
     * @param values the columns that are to be returned.
     * @param table the table to query.
     * @param condition the condition that that is set to be met. For single condition, use format "[condition] =". For multiple conditions, use format "[condition1] = ? AND [condition2] =".
     * @param comparison the values to inset into the statement. Number of conditions and comparisons must match.
     * @return the result set of the query.
     * @see #setBindVariables(String...)
     */
// Need to make this more like the insert statement.
    public static ResultSet selectConditional(String values, String table, String condition, String ... comparison) {
        sql = "SELECT " + values + " FROM " + table + " WHERE " + condition + " ?";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            setBindVariables(comparison);
            return statement.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Insert a singular row into a table.
     * @param table the table to insert the row on.
     * @param columns the columns that will insert values into.
     * @param values the values that need to be inserted.
     * @return true if insert was successful and false if not.
     * @see #setBindVariables(String...)
     */
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

    /**
     * Update a row(s) on a table matching the condition specified
     * @param table the table to update the row on.
     * @param condition The condition to validate against. Format must be "[condition] = [value]"
     * @param columns the columns in the row to update. String must be comma delimited.
     * @param values the values to update in the specified columns.
     * @return true if successful, false if columns lenght and values length don't match or if statement is not successfully executed.
     * @see #setBindVariables(String...)
     */
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
            return false;
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

    /**
     * Deletes a row from a table matching the specified condition(s). Currently only supporting one condition.
     * @param table the table to delete a row from.
     * @param condition the condition that must be met for the row to be deleted. Format must be "[condition] ="
     * @param comparison the comparison value that is paired with the condition.
     * @return true if successful and false if not.
     */
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

    /**
     * Loops through an array of Strings to try and set three different types of SQL variables, Integer, Timestamp and String.
     * @param values the array of Strings to be parsed for setting the SQL variables.
     * @throws SQLException when unable to set a value to a Bind variable.
     */
    private static void setBindVariables(String ... values) throws SQLException {
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
