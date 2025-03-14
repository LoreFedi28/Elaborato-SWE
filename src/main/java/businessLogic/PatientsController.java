package businessLogic;

import dao.PatientDAO;
import domainModel.Patient;
import java.util.logging.Logger;

public class PatientsController extends PeopleController<Patient> {
    private static final Logger logger = Logger.getLogger(PatientsController.class.getName());

    public PatientsController(PatientDAO patientDAO) {
        super(patientDAO);
    }

    public void addPerson(String cf, String name, String surname, String urgencyLevel) throws IllegalArgumentException {
        if (cf == null || cf.isBlank() || name == null || name.isBlank() ||
                surname == null || surname.isBlank() || urgencyLevel == null || urgencyLevel.isBlank()) {
            throw new IllegalArgumentException("I campi CF, nome, cognome e livello di urgenza non possono essere vuoti.");
        }

        try {
            Patient p = new Patient(cf, name, surname, urgencyLevel);
            super.addPerson(p);
        } catch (Exception e) {
            logger.severe("Errore durante l'aggiunta del paziente con CF: " + cf + " - " + e.getMessage());
            throw new RuntimeException("Errore nell'aggiunta del paziente", e);
        }
    }
}