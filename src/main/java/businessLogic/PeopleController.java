package businessLogic;

import dao.DAO;
import domainModel.Person;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import static java.util.Collections.unmodifiableList;

public abstract class PeopleController<T extends Person> {
    private final DAO<T, String> dao;
    private static final Logger logger = Logger.getLogger(PeopleController.class.getName());

    PeopleController(DAO<T, String> dao) {
        this.dao = dao;
    }

    protected String addPerson(T newPerson) throws IllegalArgumentException, SQLException {
        if (newPerson == null || newPerson.getCF() == null || newPerson.getCF().isBlank()) {
            throw new IllegalArgumentException("La persona o il codice fiscale non possono essere nulli o vuoti.");
        }
        try {
            this.dao.insert(newPerson);
            return newPerson.getCF();
        } catch (SQLException e) {
            logger.severe("Errore SQL durante l'inserimento della persona con CF: " + newPerson.getCF() + " - " + e.getMessage());
            throw e;
        }
    }

    public boolean removePerson(String cf) throws IllegalArgumentException {
        if (cf == null || cf.isBlank()) {
            throw new IllegalArgumentException("Il codice fiscale non può essere nullo o vuoto.");
        }
        try {
            return this.dao.delete(cf);
        } catch (Exception e) {
            logger.severe("Errore durante la rimozione della persona con CF: " + cf + " - " + e.getMessage());
            return false;
        }
    }

    public Optional<T> getPerson(String cf) throws IllegalArgumentException {
        if (cf == null || cf.isBlank()) {
            throw new IllegalArgumentException("Il codice fiscale non può essere nullo o vuoto.");
        }
        try {
            return Optional.ofNullable(this.dao.get(cf));
        } catch (Exception e) {
            logger.severe("Errore durante il recupero della persona con CF: " + cf + " - " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<T> getAll() {
        try {
            return unmodifiableList(this.dao.getAll());
        } catch (Exception e) {
            logger.severe("Errore durante il recupero dell'elenco delle persone - " + e.getMessage());
            return List.of(); // Restituisce una lista vuota in caso di errore
        }
    }
}