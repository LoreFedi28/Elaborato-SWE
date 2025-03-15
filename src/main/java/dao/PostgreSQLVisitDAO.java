package dao;

import domainModel.State.*;
import domainModel.Visit;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLVisitDAO implements VisitDAO {

    private final TagDAO tagDAO;

    public PostgreSQLVisitDAO(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    private void setVisitState(ResultSet rs, Visit visit) throws SQLException {
        String state = rs.getString("state");
        String extraInfo = rs.getString("stateExtraInfo");

        switch (state) {
            case "Booked":
                visit.setState(new Booked(extraInfo));
                break;
            case "Cancelled":
                visit.setState(new Cancelled(extraInfo != null ? LocalDateTime.parse(extraInfo) : LocalDateTime.now()));
                break;
            case "Completed":
                visit.setState(new Completed(extraInfo != null ? LocalDateTime.parse(extraInfo) : LocalDateTime.now()));
                break;
            default:
                visit.setState(new Available());
        }
    }

    @Override
    public Visit get(Integer idVisit) throws SQLException {
        String query = "SELECT * FROM visits WHERE idVisit = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idVisit);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Visit visit = new Visit(
                            rs.getInt("idVisit"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getTimestamp("startTime").toLocalDateTime(),
                            rs.getTimestamp("endTime").toLocalDateTime(),
                            rs.getDouble("price"),
                            rs.getString("doctorCF")
                    );
                    setVisitState(rs, visit);
                    visit.getTags().addAll(tagDAO.getTagsByVisit(idVisit));
                    return visit;
                }
            }
        }
        return null;
    }

    @Override
    public List<Visit> getAll() throws SQLException {
        List<Visit> visits = new ArrayList<>();
        String query = "SELECT * FROM visits";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Visit visit = new Visit(
                        rs.getInt("idVisit"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("startTime").toLocalDateTime(),
                        rs.getTimestamp("endTime").toLocalDateTime(),
                        rs.getDouble("price"),
                        rs.getString("doctorCF")
                );
                setVisitState(rs, visit);
                visit.getTags().addAll(tagDAO.getTagsByVisit(visit.getIdVisit()));
                visits.add(visit);
            }
        }
        return visits;
    }

    @Override
    public void insert(Visit visit) throws SQLException {
        String query = "INSERT INTO visits (title, description, startTime, endTime, price, doctorCF, state, stateExtraInfo) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING idVisit";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, visit.getTitle());
            ps.setString(2, visit.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(visit.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(visit.getEndTime()));
            ps.setDouble(5, visit.getPrice());
            ps.setString(6, visit.getDoctorCF());
            ps.setString(7, visit.getState());
            ps.setString(8, visit.getStateExtraInfo());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Usa l'ID generato, ma non è necessario settarlo nell'oggetto visit
                    int generatedId = rs.getInt("idVisit");
                    System.out.println("Visit ID generated: " + generatedId);
                    // Puoi anche restituire l'ID o gestirlo in altro modo se necessario
                }
            }
        }
    }

    @Override
    public void update(Visit visit) throws SQLException {
        String query = "UPDATE visits SET title = ?, description = ?, startTime = ?, endTime = ?, price = ? WHERE idVisit = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, visit.getTitle());
            ps.setString(2, visit.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(visit.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(visit.getEndTime()));
            ps.setDouble(5, visit.getPrice());
            ps.setInt(6, visit.getIdVisit());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer idVisit) throws SQLException {
        System.out.println("DEBUG: Tentativo di eliminare la visita con ID " + idVisit);

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            // Rimuoviamo prima i tag associati
            String deleteTagsQuery = "DELETE FROM visitsTags WHERE idVisit = ?";
            try (PreparedStatement psTags = conn.prepareStatement(deleteTagsQuery)) {
                psTags.setInt(1, idVisit);
                psTags.executeUpdate();
            }

            // Ora eliminiamo la visita
            String query = "DELETE FROM visits WHERE idVisit = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, idVisit);
                boolean deleted = ps.executeUpdate() > 0;
                System.out.println("DEBUG: Eliminazione riuscita? " + deleted);

                conn.commit();
                return deleted;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Visit> getDoctorVisitsByState(String doctorCF, State state) throws SQLException {
        List<Visit> visits = new ArrayList<>();
        String query = "SELECT * FROM visits WHERE doctorCF = ? AND state = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doctorCF);
            ps.setString(2, state.getState());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    visits.add(get(rs.getInt("idVisit")));
                }
            }
        }
        return visits;
    }

    @Override
    public List<Visit> getPatientBookedVisits(String patientCF) throws SQLException {
        List<Visit> visits = new ArrayList<>();
        String query = "SELECT * FROM visits WHERE state = 'Booked' AND stateExtraInfo = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, patientCF);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    visits.add(get(rs.getInt("idVisit")));
                }
            }
        }
        return visits;
    }

    @Override
    public int getNextVisitID() throws SQLException {
        String query = "SELECT COALESCE(MAX(idVisit), 0) + 1 FROM visits";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 1;
    }

    @Override
    public void changeState(Integer idVisit, State newState) throws SQLException {
        String query = "UPDATE visits SET state = ?, stateExtraInfo = ? WHERE idVisit = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, newState.getState());
            ps.setString(2, newState.getExtraInfo());
            ps.setInt(3, idVisit);
            ps.executeUpdate();
        }
    }

    @Override
    public int countDoctorVisitsByState(String doctorCF, State state) throws SQLException {
        String query = "SELECT COUNT(*) FROM visits WHERE doctorCF = ? AND state = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, doctorCF);
            ps.setString(2, state.getState());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public List<Visit> search(String query) throws SQLException {
        List<Visit> visits = new ArrayList<>();

        // Debug: stampa la query per verificare eventuali errori di sintassi
        System.out.println("Eseguo query di ricerca: " + query);

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                visits.add(get(rs.getInt("idVisit")));
            }
        }
        return visits;
    }
}