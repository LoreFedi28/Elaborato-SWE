package dao;

import java.io.*;
import java.sql.*;

public class Database {

    private static String URL = "jdbc:postgresql://localhost:5432/GestionaleVisiteMediche";
    private static final String USER = "postgres"; // Owner aggiornato
    private static final String PASSWORD = "1234"; // Password aggiornata

    private Database() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public static int initDatabase() throws IOException, SQLException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/database/schema.sql"))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            return statement.executeUpdate(resultStringBuilder.toString());
        }
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Connessione a PostgreSQL riuscita!");
        } catch (SQLException e) {
            System.out.println("Errore di connessione: " + e.getMessage());
        }
    }

    public static void setDatabase(String connectionUrl) {
        URL = connectionUrl;
    }
}