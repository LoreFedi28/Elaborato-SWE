package dao;

import domainModel.Visit;
import domainModel.State.State;
import java.sql.SQLException;
import java.util.List;

public interface VisitDAO extends DAO<Visit, Integer> {

    // Ottiene il prossimo ID disponibile per una visita
    int getNextVisitID() throws SQLException;

    // Recupera tutte le visite di un dottore con un determinato stato
    List<Visit> getDoctorVisitsByState(String dCF, State state) throws SQLException;

    // Conta quante visite ha un dottore in un determinato stato
    int countDoctorVisitsByState(String dCF, State state) throws SQLException;

    // Recupera tutte le visite prenotate da un paziente
    List<Visit> getPatientBookedVisits(String pCF) throws SQLException;

    // Cambia lo stato di una visita
    void changeState(Integer idVisit, State newState) throws SQLException;

    // Cerca visite in base a una query dinamica
    // ⚠️ ATTENZIONE: Se usata in modo improprio, potrebbe portare a SQL Injection.
    List<Visit> search(String query) throws SQLException;
}