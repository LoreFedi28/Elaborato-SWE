package businessLogic;

import domainModel.Doctor;
import dao.DoctorDAO;
import java.util.logging.Logger;
import java.util.Optional;

public class DoctorsController extends PeopleController<Doctor> {
    private static final Logger logger = Logger.getLogger(DoctorsController.class.getName());

    public DoctorsController(DoctorDAO doctorDAO) {
        super(doctorDAO);
    }

    public String addPerson(String cf, String name, String surname, String iban) {
        if (cf == null || cf.isBlank() || name == null || name.isBlank() ||
                surname == null || surname.isBlank() || iban == null || iban.isBlank()) {
            throw new IllegalArgumentException("I campi CF, nome, cognome e IBAN non possono essere vuoti.");
        }

        cf = cf.trim();
        name = name.trim();
        surname = surname.trim();
        iban = iban.trim();

        try {
            Optional<Doctor> existingDoctor = super.getPerson(cf);
            if (existingDoctor.isPresent()) {
                logger.warning("Tentativo di aggiungere un dottore con CF già esistente: " + cf);
                throw new IllegalArgumentException("Un dottore con questo codice fiscale esiste già.");
            }

            Doctor d = new Doctor(cf, name, surname, iban);
            return super.addPerson(d);
        } catch (IllegalArgumentException e) {
            throw e; // Rilancia l'eccezione se è dovuta a un errore dell'utente
        } catch (Exception e) {
            logger.severe("Errore inatteso durante l'aggiunta del dottore con CF: " + cf + " - " + e.getMessage());
            throw new RuntimeException("Si è verificato un errore interno durante l'aggiunta del dottore.");
        }
    }
}