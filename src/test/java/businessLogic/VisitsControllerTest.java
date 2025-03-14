package businessLogic;

import dao.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

class VisitsControllerTest {

    private static VisitsController visitsController;
    private static PostgreSQLTagDAO tagDAO;
    private static PostgreSQLVisitDAO visitDAO;

    private static TagsController tagsController;

    @BeforeAll
    static void setUp() throws Exception {
        Database.setDatabase("jdbc:postgresql://localhost:5432/GestionaleVisiteMediche_test");
        Database.initDatabase(true);
        tagDAO = new PostgreSQLTagDAO();
        visitDAO = new PostgreSQLVisitDAO(tagDAO);
        visitsController = new VisitsController(visitDAO, tagDAO, new DoctorsController(new PostgreSQLDoctorDAO()));
        tagsController = new TagsController(tagDAO);
    }

    @BeforeEach
    void setUpDatabase() throws Exception {
        try (Connection connection = Database.getConnection()) {
            connection.prepareStatement("DELETE FROM visits CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM tags CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM doctors CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM patients CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM visitsTags CASCADE;").executeUpdate();

            connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('doctor1', 'Doctor', 'Uno', '1234')").executeUpdate();
            connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('doctor2', 'Doctor', 'Due', '5678')").executeUpdate();

            connection.prepareStatement("INSERT INTO patients (cf, name, surname, urgencyLevel) VALUES ('patient1', 'Patient', 'Uno', 'elementary')").executeUpdate();

            connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Math', 'Specialty')").executeUpdate();
            connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Physics', 'Specialty')").executeUpdate();

            connection.prepareStatement("INSERT INTO visits (idVisit, title, description, startTime, endTime, price, doctorCF, state, stateExtraInfo) VALUES (1, 'Math Lesson', 'Math basics', '2023-10-25 10:00:00', '2023-10-25 11:00:00', 50.0, 'doctor1', 'Available', NULL)").executeUpdate();
            connection.prepareStatement("INSERT INTO visits (idVisit, title, description, startTime, endTime, price, doctorCF, state, stateExtraInfo) VALUES (2, 'Physics Lesson', 'Physics basics', '2023-10-26 10:00:00', '2023-10-26 11:00:00', 60.0, 'doctor2', 'Booked', 'patient1')").executeUpdate();
        }
    }

    @AfterEach
    void tearDownDatabase() throws Exception {
        try (Connection connection = Database.getConnection()) {
            connection.prepareStatement("DELETE FROM visits CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM tags CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM doctors CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM patients CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM visitsTags CASCADE;").executeUpdate();
        }
    }
}
