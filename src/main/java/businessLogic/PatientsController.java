package businessLogic;

import dao.PatientDAO;
import domainModel.Patient;

public class PatientsController extends PeopleController<Patient>{
    public PatientsController(PatientDAO patientDAO){
        super(patientDAO);
    }

    public String addPerson(String cf, String name, String surname, String urgencyLevel) throws Exception {
        Patient p = new Patient(cf, name, surname, urgencyLevel);
        return super.addPerson(p);
    }
}
