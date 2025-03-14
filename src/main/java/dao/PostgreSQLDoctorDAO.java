package dao;

import domainModel.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLDoctorDAO implements DoctorDAO {

    @Override
    public Doctor get(String CF) throws SQLException {
        String query = "SELECT * FROM doctors WHERE cf = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, CF);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Doctor(
                            rs.getString("cf"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("iban")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Doctor> getAll() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctors";

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getString("cf"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("iban")
                ));
            }
        }
        return doctors;
    }

    @Override
    public void insert(Doctor newDoctor) throws SQLException {  // ✅ ORA È VOID
        String query = "INSERT INTO doctors (cf, name, surname, iban) VALUES (?, ?, ?, ?)";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, newDoctor.getCF());
            ps.setString(2, newDoctor.getName());
            ps.setString(3, newDoctor.getSurname());
            ps.setString(4, newDoctor.getIban());
            ps.executeUpdate();  // ✅ NON RESTITUISCE NESSUN VALORE
        }
    }

    @Override
    public void update(Doctor updatedDoctor) throws SQLException {  // ✅ ORA È VOID
        String query = "UPDATE doctors SET name = ?, surname = ?, iban = ? WHERE cf = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, updatedDoctor.getName());
            ps.setString(2, updatedDoctor.getSurname());
            ps.setString(3, updatedDoctor.getIban());
            ps.setString(4, updatedDoctor.getCF());
            ps.executeUpdate();  // ✅ NON RESTITUISCE NESSUN VALORE
        }
    }

    @Override
    public boolean delete(String CF) throws SQLException {
        String query = "DELETE FROM doctors WHERE cf = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, CF);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Doctor> findByLastName(String lastName) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctors WHERE surname = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, lastName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doctors.add(new Doctor(
                            rs.getString("cf"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("iban")
                    ));
                }
            }
        }
        return doctors;
    }

    @Override
    public List<Doctor> findBySpecialization(String specialization) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String query = """
            SELECT d.* FROM doctors d\s
            JOIN visitsTags vt ON d.cf = vt.idVisit
            WHERE vt.tagType = 'Specialty' AND vt.tag = ?
       \s""";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, specialization);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doctors.add(new Doctor(
                            rs.getString("cf"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("iban")
                    ));
                }
            }
        }
        return doctors;
    }
}