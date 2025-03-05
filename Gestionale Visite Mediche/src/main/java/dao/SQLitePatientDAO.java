package main.java.dao;

import domainModel.Patient;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class SQLitePatientDAO implements PatientDAO {
    @Override
    public Patient get(String CF) throws SQLException{
        Connection conn = Database.getConnection();
        Patient patient = null;
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM patients WHERE cf = ?");
        ps.setString(1, CF);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            patient = new Patient(rs.getString("cf"), rs.getString("name"), rs.getString("surname"), rs.getString("level"));
        }

        rs.close();
        ps.close();
        Database.closeConnection(conn);
        return patient;
    }

    @Override
    public List<Patient> getAll() throws SQLException{
        Connection conn = Database.getConnection();
        List<Patient> patients = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM patients");

        while(rs.next()){
            patients.add(new Patient(rs.getString("cf"), rs.getString("name"), rs.getString("surname"), rs.getString("level")));
        }

        return patients;
    }

    public void insert(Patient newPatient) throws SQLException{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO patients (cf, name, surname, level) VALUES (?,?,?,?)");
        ps.setString(1, newPatient.getCF());
        ps.setString(2, newPatient.getName());
        ps.setString(3, newPatient.getSurname());
        ps.setString(4, newPatient.getLevel());

        ps.executeUpdate();
        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public void update(Patient toUpdatePatient) throws SQLException{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE patients SET name = ?, surname = ?, level = ? WHERE cf = ?");
        ps.setString(1, toUpdatePatient.getCF());
        ps.setString(2, toUpdatePatient.getName());
        ps.setString(3, toUpdatePatient.getSurname());
        ps.setString(4, toUpdatePatient.getLevel());

        ps.executeUpdate();
        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public boolean delete(String CF) throws SQLException{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM patients WHERE cf = ?");
        ps.setString(1, CF);
        int rows = ps.executeUpdate();
        ps.close();
        Database.closeConnection(conn);
        return rows > 0;
    }
}
