package businessLogic;

import domainModel.State.*;
import domainModel.Patient;
import domainModel.Visit;
import dao.VisitDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class StateController {
    private static final Logger logger = Logger.getLogger(StateController.class.getName());

    private final VisitsController visitsController;
    private final PatientsController patientsController;
    private final VisitDAO visitDAO;

    public StateController(VisitsController visitsController, PatientsController patientsController, VisitDAO visitDAO) {
        this.visitsController = visitsController;
        this.patientsController = patientsController;
        this.visitDAO = visitDAO;
    }

    public void bookVisit(String patientCF, int idVisit) throws Exception {
        Visit visit = visitsController.getVisit(idVisit);
        Patient patient = patientsController.getPerson(patientCF).orElse(null);

        if (patient == null) throw new IllegalArgumentException("Il paziente specificato non esiste.");
        if (visit == null) throw new IllegalArgumentException("L'ID della visita specificata non esiste.");

        if (Objects.equals(visit.getStateExtraInfo(), patientCF)) {
            throw new RuntimeException("Il paziente è già prenotato per questa visita.");
        }

        if (!Objects.equals(visit.getState(), "Available")) {
            throw new RuntimeException("Questa visita non può essere prenotata.");
        }

        for (Visit bookedVisit : this.getPatientBookedVisits(patientCF)) {
            if ((bookedVisit.getStartTime().isBefore(visit.getEndTime()) || bookedVisit.getStartTime().equals(visit.getEndTime()))
                    && (bookedVisit.getEndTime().isAfter(visit.getStartTime()) || bookedVisit.getEndTime().equals(visit.getStartTime()))) {
                throw new RuntimeException("Il paziente è già occupato in questo intervallo di tempo (visita #" + bookedVisit.getIdVisit() + ").");
            }
        }

        Booked book = new Booked(patient.getCF());
        visitDAO.changeState(idVisit, book);
        logger.info("Visita " + idVisit + " prenotata con successo per il paziente " + patientCF);
    }

    public void deleteBooking(String patientCF, int idVisit) throws Exception {
        Visit visit = visitsController.getVisit(idVisit);
        if (visit == null) throw new IllegalArgumentException("L'ID della visita specificata non esiste.");

        Patient patient = patientsController.getPerson(patientCF).orElse(null);
        if (patient == null) throw new IllegalArgumentException("Il paziente specificato non esiste.");

        if (!Objects.equals(visit.getState(), "Booked")) {
            throw new RuntimeException("La prenotazione non può essere annullata perché la visita non è nello stato 'Booked'.");
        }

        if (Objects.equals(visit.getStateExtraInfo(), patientCF)) {
            Available av = new Available();
            visitDAO.changeState(idVisit, av);
            logger.info("Prenotazione per la visita " + idVisit + " eliminata con successo per il paziente " + patientCF);
        } else {
            throw new RuntimeException("Il paziente " + patientCF + " non è prenotato per la visita " + idVisit + ".");
        }
    }

    public List<Visit> getPatientBookedVisits(String patientCF) throws Exception {
        return visitDAO.getPatientBookedVisits(patientCF);
    }

    public void cancelVisit(int idVisit) throws Exception {
        Visit visit = visitsController.getVisit(idVisit);
        if (visit == null) throw new IllegalArgumentException("L'ID della visita specificata non esiste.");

        if (Objects.equals(visit.getState(), "Completed")) {
            throw new RuntimeException("Non puoi cancellare una visita già completata.");
        }

        Cancelled cancelledState = new Cancelled(LocalDateTime.now());
        visitDAO.changeState(idVisit, cancelledState);
        logger.info("Visita " + idVisit + " cancellata con successo.");
    }

    public void completeVisit(int idVisit) throws Exception {
        Visit visit = visitsController.getVisit(idVisit);
        if (visit == null) throw new IllegalArgumentException("L'ID della visita specificata non esiste.");

        if (!Objects.equals(visit.getState(), "Booked")) {
            throw new RuntimeException("Non puoi segnare come completata una visita che non è prenotata.");
        }

        Completed completedState = new Completed(LocalDateTime.now());
        visitDAO.changeState(idVisit, completedState);
        logger.info("Visita " + idVisit + " completata con successo.");
    }
}