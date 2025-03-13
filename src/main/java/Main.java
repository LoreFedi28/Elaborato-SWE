import businessLogic.*;
import dao.*;
import domainModel.*;
import domainModel.Tags.*;
import domainModel.Search.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception{
        Database.setDatabase("main.db");
        Database.initDatabase();

        // DAOS
        DoctorDAO doctorDAO = new PostgreSQLDoctorDAO();
        PatientDAO patientDAO = new PostgreSQLPatientDAO();
        TagDAO tagDAO = new PostegreSQLTagDAO();
        VisitDAO visitDAO = new PostgreSQLVisitDAO(tagDAO);

        //Controllers
        DoctorsController doctorsController = new DoctorsController(doctorDAO);
        PatientsController patientsController = new PatientsController(patientDAO);
        TagsController tagsController = new TagsController(tagDAO);
        VisitsController visitsController = new VisitsController(visitDAO, tagDAO, doctorsController);
        StateController stateController = new StateController(visitsController, patientsController, visitDAO);


        // Add sample doctors
        doctorsController.addPerson("doctor1", "Doctor", "Uno", "1234A");
        doctorsController.addPerson("doctor2", "Doctor", "Due", "5678B");
        doctorsController.addPerson("doctor3", "Doctor", "Tre", "1011C");

        // Add sample patient
        patientsController.addPerson("patient1", "Patient", "Uno", "Elementary");
        patientsController.addPerson("patient2", "Patient", "Due", "Elementary");
        patientsController.addPerson("patient3", "Patient", "Tre", "Elementary");

        // Add sample tags
        Tag tagZoneFirenze = tagsController.createTag("Firenze", "Zone");
        Tag tagZoneMilano = tagsController.createTag("Milano", "Zone");
        Tag tagZoneParma = tagsController.createTag("Parma", "Zone");

        Tag tagSpecialtyCardiologia = tagsController.createTag("Cardiologia", "Specialty");
        Tag tagSpecialtyOncologia = tagsController.createTag("Oncologia", "Specialty");
        Tag tagSpecialtyOrtopedia = tagsController.createTag("Ortopedia", "Specialty");
        Tag tagSpecialtyPneumologia = tagsController.createTag("Pneumologia", "Specialty");
        Tag tagSpecialtyDiabetologia = tagsController.createTag("Diabetologia", "Specialty");


        // Creates a list of tags
        List<Tag> tags1 = new ArrayList<>();
        tags1.add(tagZoneFirenze);
        tags1.add(tagSpecialtyCardiologia);
        tags1.add(tagSpecialtyDiabetologia);
        tags1.add(tagSpecialtyPneumologia);
        tags1.add(tagSpecialtyOrtopedia);

        List<Tag> tags2 = new ArrayList<>();
        tags2.add(tagZoneMilano);
        tags2.add(tagSpecialtyOncologia);
        tags2.add(tagSpecialtyCardiologia);
        tags2.add(tagSpecialtyOrtopedia);

        List<Tag> tags3 = new ArrayList<>();
        tags3.add(tagZoneParma);
        tags3.add(tagSpecialtyOncologia);

        List<Tag> tags4 = new ArrayList<>();
        tags4.add(tagZoneMilano);
        tags4.add(tagSpecialtyOrtopedia);

        List<Tag> tags5 = new ArrayList<>();
        tags5.add(tagZoneFirenze);
        tags5.add(tagSpecialtyOrtopedia);


        // Add sample visits
        int vCarFi = visitsController.addVisit("Visita Cardiologica", "Prima visita cardiologica a Firenze", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 25, "doctor1", tags1);
        int vOncMi = visitsController.addVisit("Visita Oncologica", "Prima visita oncologica a Milano", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 25.00, "doctor2", tags2);
        int vOncPr = visitsController.addVisit("Visita Oncologica", "Prima visita oncologica a Parma", LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5), 30.00, "doctor2", tags3);
        int vOrtMi = visitsController.addVisit("Visita Ortopedica", "Prima visita ortopedica a Milano", LocalDateTime.now().plusHours(23), LocalDateTime.now().plusHours(24), 35.00, "doctor2", tags4);
        int vOrtFi = visitsController.addVisit("Visita Ortopedica", "Prima visita ortopedica a Firenze", LocalDateTime.now().plusHours(27), LocalDateTime.now().plusHours(29), 18.00, "doctor2", tags5);

        // DECORATOR
        System.out.println("Searching for visits with Cardiologia and Ortopedia tags and price less than 29.00. Query generated:");
        List<Visit> visits = visitsController.search(
                new DecoratorSearchPrice
                        (new DecoratorSearchSpecialty(
                                new DecoratorSearchSpecialty(
                                        new SearchConcrete(),
                                        "Cardiologia"),
                                "Ortopedia"),
                                29.00));

        System.out.println("\nResults:");
        for (Visit v : visits){
            System.out.println(v.toString());
        }

        System.out.println("\nSearching for visits starting from one hour ago. Query generated:");

        List<Visit> visits2= visitsController.search(new DecoratorSearchStartTime(
                new SearchConcrete(), LocalDateTime.now().plusHours(5)));

        System.out.println("\nResults:");
        for (Visit v : visits2){
            System.out.println(v.toString());
        }
    }
}