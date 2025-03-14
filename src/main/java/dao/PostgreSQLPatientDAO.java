package dao;

import domainModel.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLPatientDAO implements PatientDAO {

    @Override
    public Patient get(String CF) throws SQLException {
        String query = "SELECT * FROM patients WHERE cf = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, CF);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                            rs.getString("cf"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("urgencyLevel")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Patient> getAll() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                patients.add(new Patient(
                        rs.getString("cf"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("urgencyLevel")
                ));
            }
        }
        return patients;
    }

    @Override
    public void insert(Patient newPatient) throws SQLException {
        String query = "INSERT INTO patients (cf, name, surname, urgencyLevel) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newPatient.getCF());
            ps.setString(2, newPatient.getName());
            ps.setString(3, newPatient.getSurname());
            ps.setString(4, newPatient.getUrgencyLevel());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Patient updatedPatient) throws SQLException {
        String query = "UPDATE patients SET name = ?, surname = ?, urgencyLevel = ? WHERE cf = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, updatedPatient.getName());
            ps.setString(2, updatedPatient.getSurname());
            ps.setString(3, updatedPatient.getUrgencyLevel());
            ps.setString(4, updatedPatient.getCF());
            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(String CF) throws SQLException {
        String query = "DELETE FROM patients WHERE cf = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, CF);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Patient> findByLastName(String lastName) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients WHERE surname = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, lastName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    patients.add(new Patient(
                            rs.getString("cf"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("urgencyLevel")
                    ));
                }
            }
        }
        return patients;
    }

    @Override
    public List<Patient> findByAppointmentDate(String appointmentDate) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String query = """
            SELECT p.* FROM patients p 
            JOIN visits v ON p.cf = v.stateExtraInfo 
            WHERE v.startTime::date = ? AND v.state = 'Booked'""";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, Date.valueOf(appointmentDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    patients.add(new Patient(
                            rs.getString("cf"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("urgencyLevel")
                    ));
                }
            }
        }
        return patients;
    }
}