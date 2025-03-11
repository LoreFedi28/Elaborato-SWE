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


    public VisitsController(VisitDAO visitDAO, TagDAO tagDAO, PeopleController<Doctor> doctorsController){
        this.visitDAO = visitDAO;
        this.tagDAO =  tagDAO;
        this.doctorsController = doctorsController;
    }

    public int addVisit(String title, String description, LocalDateTime startTime, LocalDateTime endTime, double price, String doctorCF, List<Tag> tags) throws Exception{
        System.out.println("DEBUG: Cerco il medico con CF: " + doctorCF);

        Doctor doctor = doctorsController.getPerson(doctorCF);
        if (doctor == null) {
            System.out.println("DEBUG: Il medico con CF " + doctorCF + " non esiste nel database!");
            throw new IllegalArgumentException("Doctor not found");
        }

        for (Visit v : this.visitDAO.getAll()) {
            if (v.getDoctorCF().equals(doctorCF)) {
                if ((v.getStartTime().isBefore(endTime) || v.getStartTime().equals(endTime))
                        && (v.getEndTime().isAfter(startTime) || v.getEndTime().equals(startTime)))
                    throw new RuntimeException("The given doctor is already occupied in the given time range (in course #" + v.getIdVisit() + ")");
            }
        }

        Visit v = new Visit(visitDAO.getNextVisitID(), title, description, startTime, endTime, price, doctorCF);
        for (Tag t : tags){
            v.addTag(t);
        }
        visitDAO.insert(v);
        return v.getIdVisit();
    }

    public void updateVisit(int idVisit, String newTitle, String newDescription, LocalDateTime newStartTime, LocalDateTime newEndTime, double newPrice, String doctorCF) throws Exception {
        if (this.visitDAO.get(idVisit) == null) throw new IllegalArgumentException("You cannot modify a visit that doesn't exist.");

        Visit v = new Visit(idVisit, newTitle, newDescription, newStartTime, newEndTime, newPrice, doctorCF);
        this.visitDAO.update(v);
    }

    public boolean removeVisit(int idVisit) throws Exception{
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

    public void attachTag(int idVisit, Tag tagToAttach) throws Exception {
        if (this.tagDAO.getTag(tagToAttach.getTag(), tagToAttach.getTypeOfTag()) != null){
            this.tagDAO.attachTag(idVisit, tagToAttach);
        }else
            throw new IllegalArgumentException("This tag does not exist");
    }

    public boolean detachTag(int idVisit, Tag tagToDetach) throws Exception{
        List<Tag> tags = this.tagDAO.getTagsByVisit(idVisit);
        for (Tag t : tags){
            if (Objects.equals(t.getTypeOfTag(), tagToDetach.getTypeOfTag()) && Objects.equals(t.getTag(), tagToDetach.getTag())){
                return this.tagDAO.detachTag(idVisit, tagToDetach);
            }
        }
        return false;
    }

    public List<Visit> search(Search search) throws Exception{
        System.out.println(search.getSearchQuery());
        return visitDAO.search(search.getSearchQuery());
    }
}
