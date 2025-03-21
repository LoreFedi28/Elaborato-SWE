package businessLogic;

import dao.VisitDAO;
import dao.TagDAO;
import domainModel.Visit;
import domainModel.Search.Search;
import domainModel.State.*;
import domainModel.Tags.*;
import domainModel.Doctor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;

public class VisitsController {
    private final VisitDAO visitDAO;
    private final TagDAO tagDAO;
    private final PeopleController<Doctor> doctorsController;

    public VisitsController(VisitDAO visitDAO, TagDAO tagDAO, PeopleController<Doctor> doctorsController) {
        this.visitDAO = visitDAO;
        this.tagDAO = tagDAO;
        this.doctorsController = doctorsController;
    }

    public int addVisit(String title, String description, LocalDateTime startTime, LocalDateTime endTime, double price, String doctorCF, List<Tag> tags) throws Exception {
        System.out.println("DEBUG: Cerco il medico con CF: " + doctorCF);

        Doctor doctor = doctorsController.getPerson(doctorCF).orElse(null);
        if (doctor == null) {
            System.out.println("DEBUG: Il medico con CF " + doctorCF + " non esiste nel database!");
            throw new IllegalArgumentException("Medico non trovato");
        }

        for (Visit v : this.visitDAO.getAll()) {
            if (v.getDoctorCF().equals(doctorCF)) {
                if ((v.getStartTime().isBefore(endTime) || v.getStartTime().equals(endTime))
                        && (v.getEndTime().isAfter(startTime) || v.getEndTime().equals(startTime))) {
                    throw new RuntimeException("Il medico selezionato è già occupato nell'intervallo di tempo specificato (in visita #" + v.getIdVisit() + ")");
                }
            }
        }

        int visitId = visitDAO.getNextVisitID();
        Visit v = new Visit(visitId, title, description, startTime, endTime, price, doctorCF);
        visitDAO.insert(v);

        // Attacca i tag alla visita (salvando nel database)
        for (Tag t : tags) {
            System.out.println("DEBUG: Attaccando tag " + t.getTag() + " alla visita " + visitId);
            attachTag(visitId, t);
        }

        return visitId;
    }

    public void updateVisit(int idVisit, String newTitle, String newDescription, LocalDateTime newStartTime, LocalDateTime newEndTime, double newPrice, String doctorCF) throws Exception {
        if (this.visitDAO.get(idVisit) == null)
            throw new IllegalArgumentException("Non è possibile modificare una visita che non esiste.");

        Visit v = new Visit(idVisit, newTitle, newDescription, newStartTime, newEndTime, newPrice, doctorCF);
        this.visitDAO.update(v);
    }

    public boolean removeVisit(int idVisit) throws Exception {
        return visitDAO.delete(idVisit);
    }

    public Visit getVisit(int idVisit) throws Exception {
        return visitDAO.get(idVisit);
    }

    public List<Visit> getAll() throws Exception {
        return unmodifiableList(this.visitDAO.getAll());
    }

    public List<Visit> getDoctorVisitByState(String doctorCF, State state) throws Exception {
        return visitDAO.getDoctorVisitsByState(doctorCF, state);
    }

    public void changeState(int visitId, State newState) throws Exception {
        Visit visit = visitDAO.get(visitId);
        if (visit == null) {
            throw new IllegalArgumentException("La visita con ID " + visitId + " non esiste.");
        }

        visitDAO.changeState(visitId, newState);
    }

    public void attachTag(int idVisit, Tag tagToAttach) throws Exception {
        System.out.println("DEBUG: Tentativo di attaccare il tag '" + tagToAttach.getTag() + "' di tipo '" + tagToAttach.getTypeOfTag() + "' alla visita con ID " + idVisit);

        // Controlla se il tag è già associato alla visita
        List<Tag> existingTags = this.tagDAO.getTagsByVisit(idVisit);
        for (Tag existingTag : existingTags) {
            if (existingTag.getTag().equals(tagToAttach.getTag()) && existingTag.getTypeOfTag().equals(tagToAttach.getTypeOfTag())) {
                System.out.println("DEBUG: Il tag '" + tagToAttach.getTag() + "' è già associato alla visita con ID " + idVisit + ". Nessuna azione necessaria.");
                return;  // Esce senza aggiungere un duplicato
            }
        }

        // Controlla se il tag esiste nel database, altrimenti lo crea
        Tag existingTag = this.tagDAO.getTag(tagToAttach.getTag(), tagToAttach.getTypeOfTag());
        if (existingTag == null) {
            System.out.println("DEBUG: Il tag '" + tagToAttach.getTag() + "' non esiste. Lo creo ora...");
            this.tagDAO.addTag(tagToAttach);
        }

        // Ora il tag esiste ed è nuovo per la visita, quindi può essere associato
        this.tagDAO.attachTag(idVisit, tagToAttach);
        System.out.println("DEBUG: Tag '" + tagToAttach.getTag() + "' attaccato con successo alla visita con ID " + idVisit);
    }

    public boolean detachTag(int idVisit, Tag tagToDetach) throws Exception {
        List<Tag> tags = this.tagDAO.getTagsByVisit(idVisit);
        for (Tag t : tags) {
            if (Objects.equals(t.getTypeOfTag(), tagToDetach.getTypeOfTag()) && Objects.equals(t.getTag(), tagToDetach.getTag())) {
                return this.tagDAO.detachTag(idVisit, tagToDetach);
            }
        }
        return false;
    }

    public List<Visit> search(Search search) throws Exception {
        System.out.println(search.getSearchQuery());
        return visitDAO.search(search.getSearchQuery());
    }
}