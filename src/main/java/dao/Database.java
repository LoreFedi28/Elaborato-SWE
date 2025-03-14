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

    // Metodo con argomento booleano per decidere quale script usare
    public static void initDatabase(boolean isTest) throws IOException, SQLException {
        StringBuilder resultStringBuilder = new StringBuilder();
        // Usa lo script di test se isTest è true, altrimenti usa lo script principale
        String scriptPath = isTest ? "src/test/resources/database/schema_test.sql" : "src/main/resources/database/schema.sql";

        try (BufferedReader br = new BufferedReader(new FileReader(scriptPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(resultStringBuilder.toString());
        }
    }

    public static void main(String[] args) {
        try {
            // Passa 'true' per usare lo script di test, 'false' per quello di produzione
            Database.initDatabase(true);  // Usa il database di test
            System.out.println("Database di test inizializzato.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setDatabase(String connectionUrl) {
        URL = connectionUrl;
    }
}