package businessLogic;

import domainModel.Doctor;
import main.java.businessLogic.PeopleController;

public class DoctorsController extends PeopleController<Doctor> {
    public String addPerson(String cf, String name, String surname, String iban) throws Exception {
        Doctor d = new Doctor(cf, name, surname, iban);
        return super.addPerson(d);
    }
}
