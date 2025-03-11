package dao;

import domainModel.Visit;
import domainModel.State.*;
import domainModel.Tags.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostgreSQLVisitDAO implements VisitDAO {

    private final TagDAO tagDAO;

    public PostgreSQLVisitDAO(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    private void setVisitState(ResultSet rs, Visit visit) throws Exception{
        if(Objects.equals(rs.getString("state"), "Booked")){
            String a = rs.getString("stateExtraInfo");
            Booked booked = new Booked(a);
            visit.setState(booked);
        } else if(Objects.equals(rs.getString("state"), "Cancelled")){
            LocalDateTime ldt = LocalDateTime.parse(rs.getString("stateExtraInfo"));
            Cancelled cancelled = new Cancelled(ldt);
            visit.setState(cancelled);
        } else{
            Available available = new Available();
            visit.setState(available);
        }
    }

    @Override
    public Visit get(Integer idVisit) throws Exception{
        Connection conn = Database.getConnection();
        Visit visit = null;
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM visits WHERE visitID = ?");
        ps.setInt(1, idVisit);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            visit = new Visit(rs.getInt("idVisit"), rs.getString("title"), rs.getString("description"), LocalDateTime.parse(rs.getString("startTime")), LocalDateTime.parse(rs.getString("endTime")), rs.getDouble("price"), rs.getString("doctorCF"));
            this.setVisitState(rs, visit);
            List<Tag> visitTags = this.tagDAO.getTagsByVisit(visit.getIdVisit());
            for(Tag tag : visitTags){
                visit.addTag(tag);
            }
        }
        rs.close();
        ps.close();
        Database.closeConnection(conn);
        return visit;
    }

    @Override
    public List<Visit> getAll() throws Exception{
        Connection conn = Database.getConnection();
        List<Visit> visits = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM visits");

        while(rs.next()){
            Visit visit = new Visit(rs.getInt("idVisit"), rs.getString("title"), rs.getString("description"), LocalDateTime.parse(rs.getString("startTime")), LocalDateTime.parse(rs.getString("endTime")), rs.getDouble("price"), rs.getString("doctorCF"));
            this.setVisitState(rs, visit);
            List<Tag> visitTags = this.tagDAO.getTagsByVisit(visit.getIdVisit());
            for(Tag tag : visitTags){
                visit.addTag(tag);
            }
            visits.add(visit);
        }
        rs.close();
        statement.close();
        Database.closeConnection(conn);
        return visits;
    }

    @Override
    public void insert(Visit visit) throws Exception{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO visits(title, description, startTime, endTime, price, doctorCF, state, stateExtraInfo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, visit.getTitle());
        ps.setString(2, visit.getDescription());
        ps.setString(3, visit.getStartTime().toString());
        ps.setString(4, visit.getEndTime().toString());
        ps.setDouble(5, visit.getPrice());
        ps.setString(6, visit.getDoctorCF());
        ps.setString(7, visit.getState());
        ps.setString(8, visit.getStateExtraInfo());

        ps.executeUpdate();
        ps.close();
        for(Tag tag: visit.getTags()){
            if(this.tagDAO.getTag(tag.getTag(), tag.getTypeOfTag()) == null){
                this.tagDAO.addTag(tag);
            }
        }
        Database.closeConnection(conn);
    }

    @Override
    public void update(Visit visit) throws SQLException{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE visits SET title = ?, description = ?, startTime = ?, endTime = ?, price = ? WHERE visitID = ?");
        ps.setString(1, visit.getTitle());
        ps.setString(2, visit.getDescription());
        ps.setString(3, visit.getStartTime().toString());
        ps.setString(4,visit.getEndTime().toString());
        ps.setDouble(5, visit.getPrice());
        ps.setInt(6, visit.getIdVisit());
        ps.executeUpdate();
        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public boolean delete(Integer idVisit) throws Exception{
        Visit visit = get(idVisit);
        if(visit == null){
            return false;
        }
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM visits WHERE visitID = ?");
        ps.setInt(1, idVisit);
        int rows = ps.executeUpdate();
        ps.close();
        Database.closeConnection(conn);
        return rows > 0;
    }

    @Override
    public List<Visit> getDoctorVisitsByState(String doctorCF, State state) throws Exception{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM visits WHERE doctorCF = ? AND state = ?");
        ps.setString(1, doctorCF);
        ps.setString(2, state.getState());
        ResultSet rs = ps.executeQuery();

        List<Visit> visits = new ArrayList<>();
        while (rs.next()){
            visits.add(this.get(rs.getInt("idVisit")));
        }

        rs.close();
        ps.close();
        Database.closeConnection(conn);
        return visits;
    }

    @Override
    public List<Visit> getPatientBookedVisits(String patientCF) throws Exception{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM visits WHERE state = ? AND stateExtraInfo = ?");
        ps.setString(1, "Booked");
        ps.setString(2, patientCF);
        ResultSet rs = ps.executeQuery();

        List<Visit> visits = new ArrayList<>();
        while (rs.next()){
            visits.add(this.get(rs.getInt("idVisit")));
        }

        rs.close();
        ps.close();
        Database.closeConnection(conn);
        return visits;
    }

    @Override
    public int getNextVisitID() throws SQLException{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT seq FROM sqlite_sequence WHERE name = ?");
        ps.setString(1, "visits");
        ResultSet rs = ps.executeQuery();
        int nextID = 1;
        if(rs.next()){
            nextID = rs.getInt("seq") + 1;
        }

        rs.close();
        ps.close();
        Database.closeConnection(conn);
        return nextID;
    }

    private int getLastVisitID() throws SQLException{
        Connection conn = Database.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(visitID) FROM main.visits");
        int id = rs.getInt(1);

        rs.close();
        stmt.close();
        Database.closeConnection(conn);
        return id;
    }

    @Override
    public void changeState(Integer idVisit, State newState) throws SQLException{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE visits SET state = ?, stateExtraInfo = ? WHERE visitID = ?");
        ps.setString(1, newState.getState());
        ps.setString(2, newState.getExtraInfo());
        ps.setInt(3, idVisit);

        ps.executeUpdate();

        ps.close();
        Database.closeConnection(conn);
    }

    public List<Visit> search(String query) throws Exception{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        List<Visit> visits = new ArrayList<>();

        while (rs.next()){
            visits.add(this.get(rs.getInt("idVisit")));
        }

        rs.close();
        ps.close();
        Database.closeConnection(conn);
        return visits;
    }
}