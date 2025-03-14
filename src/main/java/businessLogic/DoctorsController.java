package businessLogic;

import domainModel.Doctor;
import dao.DoctorDAO;
import java.util.logging.Logger;

public class DoctorsController extends PeopleController<Doctor> {
    private static final Logger logger = Logger.getLogger(DoctorsController.class.getName());

    public DoctorsController(DoctorDAO doctorDAO) {
        super(doctorDAO);
    }

    public String addPerson(String cf, String name, String surname, String iban) throws IllegalArgumentException {
        if (cf == null || cf.isBlank() || name == null || name.isBlank() ||
                surname == null || surname.isBlank() || iban == null || iban.isBlank()) {
            throw new IllegalArgumentException("I campi CF, nome, cognome e IBAN non possono essere vuoti.");
        }

        try {
            Doctor d = new Doctor(cf, name, surname, iban);
            return super.addPerson(d);
        } catch (Exception e) {
            logger.severe("Errore durante l'aggiunta del dottore con CF: " + cf + " - " + e.getMessage());
            throw new RuntimeException("Errore nell'aggiunta del dottore", e);
        }
    }
}