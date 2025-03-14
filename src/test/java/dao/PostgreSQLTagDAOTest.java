package dao;

import domainModel.Visit;
import domainModel.Tags.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PostgreSQLTagDAOTest {
    private PostgreSQLTagDAO tagDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the database for testing
        Database.setDatabase("jdbc:postgresql://localhost:5432/GestionaleVisiteMediche_test");
        Database.initDatabase(true);
    }

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            tagDAO = new PostgreSQLTagDAO();
            connection.prepareStatement("DELETE FROM tags CASCADE;").executeUpdate();
        }
    }
}
