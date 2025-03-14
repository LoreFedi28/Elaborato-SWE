package dao;

import domainModel.Doctor;
import java.sql.SQLException;
import java.util.List;

public interface DoctorDAO extends DAO<Doctor, String> {

    // Trova tutti i dottori con un determinato cognome
    List<Doctor> findByLastName(String lastName) throws SQLException;

    // Trova tutti i dottori con una determinata specializzazione
    List<Doctor> findBySpecialization(String specialization) throws SQLException;
}