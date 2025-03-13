package dao;

import domainModel.Doctor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class PostgreSQLDoctorDAOTest {
    private PostgreSQLDoctorDAO doctorDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the test database
        Database.setDatabase("jdbc:postgresql://localhost:5432/testdb");
        Database.initDatabase();
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
