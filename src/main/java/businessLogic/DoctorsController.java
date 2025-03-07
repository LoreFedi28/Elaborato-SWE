package businessLogic;

import domainModel.Doctor;
import dao.DoctorDAO;

public class DoctorsController extends PeopleController<Doctor> {
    public DoctorsController(DoctorDAO doctorDAO){
        super(doctorDAO);
    }
    public String addPerson(String cf, String name, String surname, String iban) throws Exception {
        Doctor d = new Doctor(cf, name, surname, iban);
        return super.addPerson(d);
    }
}
