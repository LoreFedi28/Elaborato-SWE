package dao;

import java.io.*;
import java.sql.*;

public class Database {

    private static String dbName = "main.db";

    private Database(){}

    public static void setDatabase(String dbName){
        Database.dbName = dbName;
    }

    public static Connection getConnection(String dbName) throws SQLException{
        return DriverManager.getConnection("jdbc:sqlite:" + dbName);
    }

    public static Connection getConnection() throws SQLException{
        return getConnection(dbName);
    }

    public static void closeConnection(Connection connection) throws SQLException{
        connection.close();
    }

    public static int initDatabase() throws IOException, SQLException {
        StringBuilder resultStringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/database/schema.sql"));
        String line;
        while ((line = br.readLine()) != null) {
            resultStringBuilder.append(line).append("\n");
        }
        Connection connection = getConnection();
        Statement statement = getConnection().createStatement();
        int row = statement.executeUpdate(resultStringBuilder.toString());
        statement.close();
        closeConnection(connection);
        return row;
    }
}
