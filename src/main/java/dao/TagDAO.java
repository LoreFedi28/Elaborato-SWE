package dao;

import domainModel.Tags.*;
import java.sql.SQLException;
import java.util.List;

public interface TagDAO {

    // Recupera un tag specifico dato il nome e il tipo
    Tag getTag(String tag, String tagType) throws SQLException;

    // Recupera tutti i tag presenti nel database
    List<Tag> getAllTags() throws SQLException;

    // Aggiunge un nuovo tag nel database
    void addTag(Tag tag) throws SQLException;

    // Associa un tag a una visita
    void attachTag(Integer idVisit, Tag tagToAttach) throws SQLException;

    // Rimuove un tag dal database
    boolean removeTag(String tag, String tagType) throws SQLException;

    // Disassocia un tag da una visita
    boolean detachTag(Integer idVisit, Tag tagToDetach) throws SQLException;

    // Recupera tutti i tag associati a una visita
    List<Tag> getTagsByVisit(Integer idVisit) throws SQLException;

    // Verifica se un tag esiste nel database
    default boolean exists(String tag, String tagType) throws SQLException {
        return getTag(tag, tagType) != null;
    }

    // Conta quanti tag sono associati a una determinata visita
    int countTagsByVisit(Integer idVisit) throws SQLException;
}