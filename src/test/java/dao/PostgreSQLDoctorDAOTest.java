package dao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

class PostgreSQLDoctorDAOTest {
    private PostgreSQLDoctorDAO doctorDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the test database
        Database.setDatabase("jdbc:postgresql://localhost:5432/GestionaleVisiteMediche_test");
        Database.initDatabase(true);
    }

    @BeforeEach
    public void init() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            doctorDAO = new PostgreSQLDoctorDAO();
            connection.prepareStatement("DELETE FROM doctors CASCADE;").executeUpdate();
            connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('test1', 'name1', 'surname1', 'iban1')").executeUpdate();
            connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('test2', 'name2', 'surname2', 'iban2')").executeUpdate();
        }
    }
}
