package dao;

import domainModel.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class PostgreSQLPatientDAOTest {
    private PostgreSQLPatientDAO patientDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the test database
        Database.setDatabase("jdbc:postgresql://localhost:5432/testdb");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            patientDAO = new PostgreSQLPatientDAO();
            connection.prepareStatement("DELETE FROM patients CASCADE;").executeUpdate();
            connection.prepareStatement("INSERT INTO patients (cf, name, surname, urgencyLevel) VALUES ('test1', 'name1', 'surname1', 'urgencyLevel1')").executeUpdate();
            connection.prepareStatement("INSERT INTO patients (cf, name, surname, urgencyLevel) VALUES ('test2', 'name2', 'surname2', 'urgencyLevel2')").executeUpdate();
        }
    }
}
