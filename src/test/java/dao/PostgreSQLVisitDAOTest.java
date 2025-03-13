package dao;

import domainModel.Visit;
import domainModel.State.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

class PostgreSQLVisitDAOTest {
    private PostgreSQLVisitDAO visitDAO;
    private PostgreSQLTagDAO tagDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the test database
        Database.setDatabase("jdbc:postgresql://localhost:5432/testdb");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            tagDAO = new PostgreSQLTagDAO();
            visitDAO = new PostgreSQLVisitDAO(tagDAO);
            connection.prepareStatement("DELETE FROM visits CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM tags CASCADE;").executeUpdate();
        }
    }
}
