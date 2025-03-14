package dao;

import domainModel.Patient;
import java.sql.SQLException;
import java.util.List;

public interface PatientDAO extends DAO<Patient, String> {

    // Trova i pazienti per cognome
    List<Patient> findByLastName(String lastName) throws SQLException;

    // Trova i pazienti che hanno una visita programmata in una certa data
    List<Patient> findByAppointmentDate(String appointmentDate) throws SQLException;
}