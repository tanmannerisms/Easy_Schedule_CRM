package com.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class LoginQuery extends Query{
    private static ResultSet resultSet;
    private static PreparedStatement statement;
    private static String sql;

    public static String getPassword(String userName) throws SQLException{
        sql = "SELECT Password FROM users WHERE User_Name = ?";
        statement = JDBC.connection.prepareStatement(sql);
        statement.setString(1, userName);
        resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("Password");
        }
        else return null;
    }
    public static String getUsername(String userName) throws SQLException {
        sql = "SELECT User_Name FROM users WHERE User_Name = ?";
        statement = JDBC.connection.prepareStatement(sql);
        statement.setString(1, userName);
        resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("User_Name");
        }
        else return null;
    }
}
