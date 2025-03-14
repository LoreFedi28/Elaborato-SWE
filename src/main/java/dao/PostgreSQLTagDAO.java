package dao;

import domainModel.Tags.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLTagDAO implements TagDAO {

    @Override
    public Tag getTag(String tag, String tagType) throws SQLException {
        String query = "SELECT * FROM tags WHERE tag = ? AND tagType = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, tag);
            ps.setString(2, tagType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createTagFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Tag> getAllTags() throws SQLException {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT * FROM tags";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tags.add(createTagFromResultSet(rs));
            }
        }
        return tags;
    }

    @Override
    public void addTag(Tag tag) throws SQLException {
        String query = "INSERT INTO tags (tag, tagType) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, tag.getTag());
            ps.setString(2, tag.getTypeOfTag());
            ps.executeUpdate();
        }
    }

    @Override
    public void attachTag(Integer idVisit, Tag tagToAttach) throws SQLException {
        String query = "INSERT INTO visitsTags (tag, tagType, idVisit) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, tagToAttach.getTag());
            ps.setString(2, tagToAttach.getTypeOfTag());
            ps.setInt(3, idVisit);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean removeTag(String tag, String tagType) throws SQLException {
        String query = "DELETE FROM tags WHERE tag = ? AND tagType = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, tag);
            ps.setString(2, tagType);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean detachTag(Integer idVisit, Tag tagToDetach) throws SQLException {
        String query = "DELETE FROM visitsTags WHERE tag = ? AND tagType = ? AND idVisit = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, tagToDetach.getTag());
            ps.setString(2, tagToDetach.getTypeOfTag());
            ps.setInt(3, idVisit);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Tag> getTagsByVisit(Integer idVisit) throws SQLException {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT * FROM visitsTags WHERE idVisit = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idVisit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tags.add(createTagFromResultSet(rs));
                }
            }
        }
        return tags;
    }

    @Override
    public int countTagsByVisit(Integer idVisit) throws SQLException {
        String query = "SELECT COUNT(*) FROM visitsTags WHERE idVisit = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idVisit);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0; // Se la query non trova risultati, ritorna 0
    }

    /**
     * Metodo di supporto per creare un oggetto Tag dal ResultSet.
     */
    private Tag createTagFromResultSet(ResultSet rs) throws SQLException {
        String tag = rs.getString("tag");
        String tagType = rs.getString("tagType").toLowerCase();  // Convertiamo in minuscolo

        switch (tagType) {
            case "online": return new TagIsOnline(tag);
            case "urgencylevel": return new TagUrgencyLevel(tag);
            case "specialty": return new TagSpecialty(tag);
            case "zone": return new TagZone(tag);  // Ora corrisponde anche se nel DB è scritto in minuscolo
            default: throw new SQLException("Tipo di tag sconosciuto: " + tagType);
        }
    }
}