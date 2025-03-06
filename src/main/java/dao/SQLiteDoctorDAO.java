package main.java.dao;

import domainModel.Doctor;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class SQLiteDoctorDAO implements DoctorDAO {

    @Override
    public Doctor get(String CF) throws SQLException{
        Connection con = Database.getConnection();
        Doctor doctor = null;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM doctors WHERE cf = ?");
        ps.setString(1, CF);
        ResultSet rs = ps.executeQuery();

        if (rs.next()){
            doctor = new Doctor(
                    rs.getString("cf"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("iban")
            );
        }

        rs.close();
        ps.close();
        Database.closeConnection(con);
        return doctor;
    }

    @Override
    public List<Doctor> getAll() throws SQLException{
        Connection con = Database.getConnection();
        List<Doctor> doctors = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM doctors");

        while (rs.next()){
            doctors.add(new Doctor(
                    rs.getString("cf"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("iban")
            ));
        }

        return doctors;
    }

    public void insert(Doctor newDoctor) throws SQLException{
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES (?, ?, ?, ?)");
        ps.setString(1, newDoctor.getCF());
        ps.setString(2, newDoctor.getName());
        ps.setString(3, newDoctor.getSurname());
        ps.setString(4, newDoctor.getIban());

        ps.executeUpdate();
        ps.close();
        Database.closeConnection(con);
    }

    @Override
    public void update(Doctor updatedDoctor) throws SQLException{
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE doctors SET name = ?, surname = ?, iban = ? WHERE cf = ?");
        ps.setString(1, updatedDoctor.getName());
        ps.setString(2, updatedDoctor.getSurname());
        ps.setString(3, updatedDoctor.getIban());
        ps.setString(4, updatedDoctor.getCF());

        ps.executeUpdate();
        ps.close();
        Database.closeConnection(con);
    }

    @Override
    public boolean delete(String CF) throws SQLException{
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM doctors WHERE cf = ?");
        ps.setString(1, CF);
        int rows = ps.executeUpdate();
        ps.close();
        Database.closeConnection(con);
        return rows > 0;
    }
}
