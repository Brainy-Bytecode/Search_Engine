package com.example.search_engine;

import java.sql.*;

public class Database_Connection {

    String MySQLURL = "jdbc:mysql://localhost:3306/search_engine?useSSL=false";
    String DatabaseUserName = "root";
    String DatabasePassword = "hercules_rodeo19";

    Connection con = null;

    Database_Connection() throws SQLException {
        con = DriverManager.getConnection(MySQLURL, DatabaseUserName, DatabasePassword);
        System.out.println("Connection to the Database is successful.");
    }

    public ResultSet executeQuery(String query) throws SQLException {
        // for reading queries only. This is how we actually talk to the database.

        ResultSet ans = null;
        Statement statement = con.createStatement();
        ans = statement.executeQuery(query);

        return ans;
    }

    public int executeUpdate(String query) throws SQLException {
        // this must be an update query. or write query

        int ans = 0;
        Statement statement = con.createStatement();
        ans = statement.executeUpdate(query);

        return ans;
    }
}

