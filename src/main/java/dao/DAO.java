package dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, ID> {

    // Recupera un elemento dato il suo ID
    T get(ID id) throws SQLException;

    // Recupera tutti gli elementi
    List<T> getAll() throws SQLException;

    // Inserisce un nuovo elemento
    void insert(T t) throws SQLException;

    // Aggiorna un elemento esistente
    void update(T t) throws SQLException;

    // Cancella un elemento dato il suo ID, restituisce true se avviene con successo
    boolean delete(ID id) throws SQLException;

    // Metodo default per controllare se un elemento esiste
    default boolean exists(ID id) throws SQLException {
        return get(id) != null;
    }
}