import businessLogic.*;
import dao.*;
import domainModel.*;
import domainModel.Tags.*;
import domainModel.Search.*;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Imposta il database PostgreSQL
        System.out.println("Inizializzazione del database...");
        Database.initDatabase();

        // DAO (Data Access Object)
        DoctorDAO doctorDAO = new PostgreSQLDoctorDAO();
        PatientDAO patientDAO = new PostgreSQLPatientDAO();
        TagDAO tagDAO = new PostgreSQLTagDAO();
        VisitDAO visitDAO = new PostgreSQLVisitDAO(tagDAO);

        // Controller
        DoctorsController doctorsController = new DoctorsController(doctorDAO);
        PatientsController patientsController = new PatientsController(patientDAO);
        TagsController tagsController = new TagsController(tagDAO);
        VisitsController visitsController = new VisitsController(visitDAO, tagDAO, doctorsController);
        StateController stateController = new StateController(visitsController, patientsController, visitDAO);

        System.out.println("Aggiunta di medici di esempio...");
        doctorsController.addPerson("doctor1", "Giovanni", "Bianchi", "1234A");
        doctorsController.addPerson("doctor2", "Marco", "Rossi", "5678B");
        doctorsController.addPerson("doctor3", "Laura", "Verdi", "1011C");

        System.out.println("Aggiunta di pazienti di esempio...");
        patientsController.addPerson("patient1", "Mario", "Neri", "Basso");
        patientsController.addPerson("patient2", "Elisa", "Gialli", "Medio");
        patientsController.addPerson("patient3", "Antonio", "Blu", "Alto");

        System.out.println("Aggiunta di tag di esempio...");
        Tag tagZonaFirenze = tagsController.createTag("Firenze", "Zone");
        Tag tagZonaMilano = tagsController.createTag("Milano", "Zone");
        Tag tagZonaParma = tagsController.createTag("Parma", "Zone");

        Tag tagSpecialtyCardiologia = tagsController.createTag("Cardiologia", "Specialty");
        Tag tagSpecialtyOncologia = tagsController.createTag("Oncologia", "Specialty");
        Tag tagSpecialtyOrtopedia = tagsController.createTag("Ortopedia", "Specialty");
        Tag tagSpecialtyPneumologia = tagsController.createTag("Pneumologia", "Specialty");
        Tag tagSpecialtyDiabetologia = tagsController.createTag("Diabetologia", "Specialty");

        // Creazione liste di tag per le visite
        List<Tag> tags1 = List.of(tagZonaFirenze, tagSpecialtyCardiologia, tagSpecialtyDiabetologia, tagSpecialtyPneumologia, tagSpecialtyOrtopedia);
        List<Tag> tags2 = List.of(tagZonaMilano, tagSpecialtyOncologia, tagSpecialtyCardiologia, tagSpecialtyOrtopedia);
        List<Tag> tags3 = List.of(tagZonaParma, tagSpecialtyOncologia);
        List<Tag> tags4 = List.of(tagZonaMilano, tagSpecialtyOrtopedia);
        List<Tag> tags5 = List.of(tagZonaFirenze, tagSpecialtyOrtopedia);

        System.out.println("Aggiunta di visite di esempio...");
        int vCarFi = visitsController.addVisit("Visita Cardiologica", "Prima visita cardiologica a Firenze",
                LocalDateTime.now(), LocalDateTime.now().plusHours(2), 45, "doctor1", tags1);
        int vOncMi = visitsController.addVisit("Visita Oncologica", "Prima visita oncologica a Milano",
                LocalDateTime.now(), LocalDateTime.now().plusHours(2), 55.00, "doctor2", tags2);
        int vOncPr = visitsController.addVisit("Visita Oncologica", "Prima visita oncologica a Parma",
                LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5), 70.00, "doctor2", tags3);
        int vOrtMi = visitsController.addVisit("Visita Ortopedica", "Prima visita ortopedica a Milano",
                LocalDateTime.now().plusHours(23), LocalDateTime.now().plusHours(24), 35.00, "doctor2", tags4);
        int vOrtFi = visitsController.addVisit("Visita Ortopedica", "Prima visita ortopedica a Firenze",
                LocalDateTime.now().plusHours(27), LocalDateTime.now().plusHours(29), 90.00, "doctor2", tags5);

        // Composizione della ricerca combinata (tag, prezzo e startTime)
        System.out.println("\n🔍 Ricerca combinata: visite con i tag 'Cardiologia' e 'Ortopedia', prezzo inferiore a 70€ e che iniziano tra un'ora...");
        Search completeSearch = new DecoratorSearchStartTime(
                new DecoratorSearchPrice(
                        new DecoratorSearchSpecialty(
                                new DecoratorSearchSpecialty(new SearchConcrete(), "Cardiologia"),
                                "Ortopedia"),
                        70.00),
                LocalDateTime.now().plusHours(1)
        );
        List<Visit> combinedVisits = visitsController.search(completeSearch);

        System.out.println("\nRisultati trovati nella ricerca combinata:");
        for (Visit v : combinedVisits) {
            System.out.println(v);
        }

        // Se desideri mantenere anche ricerche separate, puoi lasciarle
        System.out.println("\n🔍 Ricerca separata: visite con i tag 'Cardiologia' e 'Ortopedia' e prezzo inferiore a 70€...");
        List<Visit> visitsByPriceAndTag = visitsController.search(
                new DecoratorSearchPrice(
                        new DecoratorSearchSpecialty(
                                new DecoratorSearchSpecialty(new SearchConcrete(), "Cardiologia"),
                                "Ortopedia"),
                        70.00));
        System.out.println("\nRisultati trovati (tag e prezzo):");
        for (Visit v : visitsByPriceAndTag) {
            System.out.println(v);
        }

        System.out.println("\n🔍 Ricerca separata: visite che iniziano tra un'ora...");
        List<Visit> visitsByStartTime = visitsController.search(
                new DecoratorSearchStartTime(new SearchConcrete(), LocalDateTime.now().plusHours(1)));
        System.out.println("\nRisultati trovati (start time):");
        for (Visit v : visitsByStartTime) {
            System.out.println(v);
        }
    }
}