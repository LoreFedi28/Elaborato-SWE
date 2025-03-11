package dao;

import domainModel.Tags.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostegreSQLTagDAO implements TagDAO{

    @Override
    public Tag getTag(String tag, String tagType) throws SQLException{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM tags WHERE tag = ? AND tagType = ?");
        ps.setString(1, tag);
        ps.setString(2, tagType);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            if(Objects.equals(rs.getString("tagType"), "Online")){
                TagIsOnline tio = new TagIsOnline(rs.getString("tag"));
                rs.close();
                ps.close();
                Database.closeConnection(conn);
                return tio;
            }
            else if(Objects.equals(rs.getString("tagType"), "UrgencyLevel")){
                TagUrgencyLevel tul = new TagUrgencyLevel(rs.getString("tag"));
                rs.close();
                ps.close();
                Database.closeConnection(conn);
                return tul;
            }
            else if(Objects.equals(rs.getString("tagType"), "Specialty")){
                TagSpecialty ts = new TagSpecialty(rs.getString("tag"));
                rs.close();
                ps.close();
                Database.closeConnection(conn);
                return ts;
            }
            else if(Objects.equals(rs.getString("tagType"), "Zone")){
                TagZone tz = new TagZone(rs.getString("tag"));
                rs.close();
                ps.close();
                Database.closeConnection(conn);
                return tz;
            }
        }
        rs.close();
        ps.close();
        Database.closeConnection(conn);
        return null;
    }

    @Override
    public List<Tag> getAllTags() throws SQLException{
        Connection conn = Database.getConnection();
        List<Tag> tags = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tags");

        while(rs.next()){
            if(Objects.equals(rs.getString("tagType"), "Online")){
                TagIsOnline tio = new TagIsOnline(rs.getString("tag"));
                tags.add(tio);
            }
            else if(Objects.equals(rs.getString("tagType"), "UrgencyLevel")){
                TagUrgencyLevel tul = new TagUrgencyLevel(rs.getString("tag"));
                tags.add(tul);
            }
            else if(Objects.equals(rs.getString("tagType"), "Specialty")){
                TagSpecialty ts = new TagSpecialty(rs.getString("tag"));
                tags.add(ts);
            }
            else if(Objects.equals(rs.getString("tagType"), "Zone")){
                TagZone tz = new TagZone(rs.getString("tag"));
                tags.add(tz);
            }
        }
        rs.close();
        stmt.close();
        Database.closeConnection(conn);
        return tags;
    }

    @Override
    public void addTag(Tag tag) throws SQLException{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO tags (tag, tagType) VALUES (?, ?)");
        ps.setString(1, tag.getTag());
        ps.setString(2, tag.getTypeOfTag());
        ps.executeUpdate();
        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public void attachTag(Integer idVisit, Tag tagToAttach) throws SQLException{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO visitsTags (tag, tagType, idVisit) VALUES (?, ?, ?)");
        ps.setString(1, tagToAttach.getTag());
        ps.setString(2, tagToAttach.getTypeOfTag());
        ps.setInt(3, idVisit);
        ps.executeUpdate();
        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public boolean removeTag(String tag, String tagType) throws SQLException{
        Tag tagToRemove = getTag(tag, tagType);
        if(tagToRemove == null){
            return false;
        }
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM tags WHERE tag = ? AND tagType = ?");
        ps.setString(1, tagToRemove.getTag());
        ps.setString(2, tagToRemove.getTypeOfTag());
        int rows = ps.executeUpdate();
        ps.close();
        Database.closeConnection(conn);
        return rows > 0;
    }

    public boolean detachTag(Integer idVisit, Tag tagToDetach) throws SQLException{
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM tags WHERE tag = ? AND tagType = ? AND idVisit = ?");
        ps.setString(1, tagToDetach.getTag());
        ps.setString(2, tagToDetach.getTypeOfTag());
        ps.setInt(3, idVisit);
        int rows = ps.executeUpdate();
        ps.close();
        Database.closeConnection(conn);
        return rows > 0;
    }

    @Override
    public List<Tag> getTagsByVisit(Integer idVisit) throws SQLException{
        Connection conn = Database.getConnection();
        List<Tag> tags = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM tags WHERE idVisit = ?");
        ps.setInt(1, idVisit);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            if(Objects.equals(rs.getString("tagType"), "Online")){
                TagIsOnline tio = new TagIsOnline(rs.getString("tag"));
                tags.add(tio);
            }
            else if(Objects.equals(rs.getString("tagType"), "UrgencyLevel")){
                TagUrgencyLevel tul = new TagUrgencyLevel(rs.getString("tag"));
                tags.add(tul);
            }
            else if (Objects.equals(rs.getString("tagType"), "Specialty")){
                TagSpecialty ts = new TagSpecialty(rs.getString("tag"));
                tags.add(ts);
            }
            else if (Objects.equals(rs.getString("tagType"), "Zone")){
                TagZone tz = new TagZone(rs.getString("tag"));
                tags.add(tz);
            }
        }
        return tags;
    }
}
