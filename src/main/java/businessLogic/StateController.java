package main.java.businessLogic;

import domainModel.State.*;
import domainModel.Patient;
import domainModel.Visit;
import dao.VisitDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class StateController {
    private final VisitsController visitsController;
    private final PatientsController patientsController;
    private final VisitDAO visitDAO;

    public StateController(VisitsController visitsController, PatientsController patientsController , VisitDAO visitDAO){
        this.visitsController = visitsController;
        this.patientsController = patientsController;
        this.visitDAO = visitDAO;
    }

    public void bookVisit(String patientCF, int idVisit) throws Exception {
        Visit visit = visitsController.getVisit(idVisit);
        Patient patient = patientsController.getPerson(patientCF);
        if (patient == null) throw new IllegalArgumentException("The given patient does not exist.");
        if (visit == null) throw new IllegalArgumentException("The given visit id does not exist.");

        if(Objects.equals(visit.getStateExtraInfo(), patientCF)){
            throw new RuntimeException("The given patient is already booked for this visit ");
        }


        if (!Objects.equals(visit.getState(), "Available")){
            throw new RuntimeException("You cannot book this visit");
        }

        for (Visit l: this.getPatientBookedVisits(patientCF)){
            if ((l.getStartTime().isBefore(visit.getEndTime()) || l.getStartTime().equals(visit.getEndTime()))
                    && (l.getEndTime().isAfter(visit.getStartTime()) || l.getEndTime().equals(visit.getStartTime())))
                throw new RuntimeException("The given patient is already occupied in the given time range (in course #" + l.getIdVisit() + ")");
        }

        Booked book = new Booked(patient.getCF());

        this.visitDAO.changeState(idVisit, book);
    }

    public void deleteBooking(String patientCF, int idVisit) throws Exception{
        Visit visit = visitsController.getVisit(idVisit);
        if (visit == null) throw new IllegalArgumentException("The given visit id does not exist.");

        Patient patient = patientsController.getPerson(patientCF);
        if (patient == null) throw new IllegalArgumentException("The given patient does not exist.");


        if (!Objects.equals(visit.getState(), "Booked"))
            throw new RuntimeException("The booking cannot be canceled because it is not in a 'Booked' state.");

        if(Objects.equals(visit.getStateExtraInfo(), patientCF)){
            Available av = new Available();
            this.visitDAO.changeState(idVisit, av);
        }else {
            throw new RuntimeException("Patient" + patientCF + "is not booked for visit" + idVisit + ".");
        }

    }

    public List<Visit> getPatientBookedVisits(String patientCF) throws Exception{
        return visitDAO.getPatientBookedVisits(patientCF);
    }

    public void cancelVisit(int idVisit) throws Exception {
        Visit visit = visitsController.getVisit(idVisit);
        if ( visit == null) throw new IllegalArgumentException("The given visit id does not exist.");

        if (Objects.equals(visit.getState(), "Completed")){
            throw new RuntimeException("You cannot cancel a visit that has already been completed.");
        }

        Cancelled cancelled = new Cancelled(LocalDateTime.now());
        this.visitDAO.changeState(idVisit, cancelled);
    }

    public void completeVisit(int idVisit) throws Exception{
        Visit visit = visitsController.getVisit(idVisit);
        if (visit == null) throw new IllegalArgumentException("The given visit id does not exist.");

        if (!Objects.equals(visit.getState(), "Booked")){
            throw new RuntimeException("You cannot set as completed a visit that is not booked.");
        }

        Completed completed = new Completed(LocalDateTime.now());
        this.visitDAO.changeState(idVisit, completed);
    }
}
