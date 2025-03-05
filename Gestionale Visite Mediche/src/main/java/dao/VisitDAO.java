package main.java.dao;

import domainModel.Visit;
import domainModel.State.State;

import java.util.List;

public interface VisitDAO extends DAO <Visit, Integer>{

    public int getNextVisitId() throws Exception;

    public List<Visit> getDoctorVisitsByState(String dCF, State state) throws Exception;

    public List<Visit> getPatientBookedVisits(String pCF) throws Exception;

    public void changeState(Integer idVisit, State newState) throws Exception;

    public List<Visit> search(String query) throws Exception;
}
