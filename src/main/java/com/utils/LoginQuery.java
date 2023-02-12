package com.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class LoginQuery extends Query{

    public static String select(String userName) {
        ResultSet resultSet;
        String sql = "SELECT Password FROM users WHERE User_Name = '?'";
        try {
            PreparedStatement statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, userName);
            resultSet = statement.executeQuery();
            return resultSet.getString("Password");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public int insert() {
        return 0;
    }
    public int update() {
        return 0;
    }
    public int delete() {
        return 0;
    }
}
