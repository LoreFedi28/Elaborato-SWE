package dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

class PostgreSQLPatientDAOTest {

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        Database.setDatabase("jdbc:postgresql://localhost:5432/GestionaleVisiteMediche_test");
        Database.initDatabase(true);
    }

    @BeforeEach
    public void init() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PostgreSQLPatientDAO patientDAO = new PostgreSQLPatientDAO();
            connection.prepareStatement("DELETE FROM patients CASCADE;").executeUpdate();
            connection.prepareStatement("INSERT INTO patients (cf, name, surname, urgencyLevel) VALUES ('test1', 'name1', 'surname1', 'urgencyLevel1')").executeUpdate();
            connection.prepareStatement("INSERT INTO patients (cf, name, surname, urgencyLevel) VALUES ('test2', 'name2', 'surname2', 'urgencyLevel2')").executeUpdate();
        }
    }
}
