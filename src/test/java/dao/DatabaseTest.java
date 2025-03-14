package dao;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

public class DatabaseTest {

    // Metodo che viene eseguito prima di ogni test
    @Before
    public void setUp() throws Exception {
        // Imposta il database di test
        Database.setDatabase("jdbc:postgresql://localhost:5432/GestionaleVisiteMediche_test");
        // Inizializza il database con schema_test.sql
        Database.initDatabase(true); // true significa usare il database di test
    }

    // Esegui il test di connessione al database
    @Test
    public void testDatabaseConnection() throws SQLException {
        try (Connection conn = Database.getConnection()) {
            assertNotNull("La connessione al database di test non dovrebbe essere null", conn);
        }
    }

    // Metodo che viene eseguito dopo ogni test
    @After
    public void tearDown() throws Exception {
        // Se necessario, ripristina lo stato del database o chiudi la connessione
        Database.closeConnection(Database.getConnection());
    }
}