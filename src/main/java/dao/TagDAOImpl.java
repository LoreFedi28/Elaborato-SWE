package dao;

import domainModel.Tags.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDAOImpl implements TagDAO {
    private final Connection connection;

    public TagDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Tag getTag(String tag, String tagType) throws SQLException {
        String query = "SELECT * FROM tags WHERE tag = ? AND tagType = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tag);
            stmt.setString(2, tagType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Tag(rs.getString("tag"), rs.getString("tagType"));
                }
            }
        }
        return null;
    }

    @Override
    public List<Tag> getAllTags() throws SQLException {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT * FROM tags";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tags.add(new Tag(rs.getString("tag"), rs.getString("tagType")));
            }
        }
        return tags;
    }

    @Override
    public void addTag(Tag tag) throws SQLException {
        String query = "INSERT INTO tags (tag, tagType) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tag.getTag());
            stmt.setString(2, tag.getTypeOfTag());
            stmt.executeUpdate();
        }
    }

    @Override
    public void attachTag(Integer idVisit, Tag tagToAttach) throws SQLException {
        String query = "INSERT INTO visitsTags (idVisit, tag, tagType) VALUES (?, ?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idVisit);
            stmt.setString(2, tagToAttach.getTag());
            stmt.setString(3, tagToAttach.getTypeOfTag());
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean removeTag(String tag, String tagType) throws SQLException {
        String query = "DELETE FROM tags WHERE tag = ? AND tagType = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tag);
            stmt.setString(2, tagType);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean detachTag(Integer idVisit, Tag tagToDetach) throws SQLException {
        String query = "DELETE FROM visitsTags WHERE idVisit = ? AND tag = ? AND tagType = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idVisit);
            stmt.setString(2, tagToDetach.getTag());
            stmt.setString(3, tagToDetach.getTypeOfTag());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Tag> getTagsByVisit(Integer idVisit) throws SQLException {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT t.tag, t.tagType FROM visitsTags vt JOIN tags t ON vt.tag = t.tag AND vt.tagType = t.tagType WHERE vt.idVisit = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idVisit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tags.add(new Tag(rs.getString("tag"), rs.getString("tagType")));
                }
            }
        }
        return tags;
    }

    @Override
    public int countTagsByVisit(Integer idVisit) throws SQLException {
        String query = "SELECT COUNT(*) FROM visitsTags WHERE idVisit = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idVisit);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}