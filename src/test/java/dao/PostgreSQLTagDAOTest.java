package dao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class PostgreSQLTagDAOTest {

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        Database.setDatabase("jdbc:postgresql://localhost:5432/GestionaleVisiteMediche_test");
        Database.initDatabase(true);
    }

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PostgreSQLTagDAO tagDAO = new PostgreSQLTagDAO();
            connection.prepareStatement("DELETE FROM tags CASCADE;").executeUpdate();
        }
    }
}
