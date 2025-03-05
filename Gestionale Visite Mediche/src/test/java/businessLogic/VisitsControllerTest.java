package test.java.businessLogic;

import dao.*;
import domainModel.Lesson;
import domainModel.State.*;
import domainModel.Tags.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VisitsControllerTest {

    private static LessonsController lessonsController;
    private static SQLiteTagDAO tagDAO;
    private static SQLiteLessonDAO lessonDAO;

    private static TagsController tagsController;

    @BeforeAll
    static void setUp() throws Exception {
        Database.setDatabase("test.db");
        Database.initDatabase();
        tagDAO = new SQLiteTagDAO();
        lessonDAO = new SQLiteLessonDAO(tagDAO);
        lessonsController = new LessonsController(lessonDAO, tagDAO, new TutorsController(new SQLiteTutorDAO()));
        tagsController = new TagsController(tagDAO);
    }

    @BeforeEach
    void setUpDatabase() throws Exception {
        Connection connection = Database.getConnection();

        // Pulisci le tabelle "lessons" e "tags"
        connection.prepareStatement("DELETE FROM lessons").executeUpdate();
        connection.prepareStatement("DELETE FROM tags").executeUpdate();
        connection.prepareStatement("DELETE FROM tutors").executeUpdate();
        connection.prepareStatement("DELETE FROM students").executeUpdate();
        connection.prepareStatement("DELETE FROM lessonsTags").executeUpdate();

        // Inserisci alcuni dati di test
        connection.prepareStatement("INSERT INTO tutors (cf, name, surname, iban) VALUES ('tutor1', 'Tutor', 'Uno', 1234)").executeUpdate();
        connection.prepareStatement("INSERT INTO tutors (cf, name, surname, iban) VALUES ('tutor2', 'Tutor', 'Due', 5678)").executeUpdate();

        connection.prepareStatement("INSERT INTO students (cf, name, surname, level) VALUES ('student1', 'Student', 'Uno', 'elementary')").executeUpdate();

        connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Math', 'Subject')").executeUpdate();
        connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Physics', 'Subject')").executeUpdate();

        connection.prepareStatement("INSERT INTO lessons (idLesson, title, description, startTime, endTime, price, tutorCF, state, stateExtraInfo) VALUES (1, 'Math Lesson', 'Math basics', '2023-10-25T10:00:00', '2023-10-25T11:00:00', 50.0, 'tutor1', 'Available', NULL)").executeUpdate();
        connection.prepareStatement("INSERT INTO lessons (idLesson, title, description, startTime, endTime, price, tutorCF, state, stateExtraInfo) VALUES (2, 'Physics Lesson', 'Physics basics', '2023-10-26T10:00:00', '2023-10-26T11:00:00', 60.0, 'tutor2', 'Booked', 'student1')").executeUpdate();
    }

    @AfterEach
    void tearDownDatabase() throws Exception {
        Connection connection = Database.getConnection();

        // Ripulisci le tabelle "lessons" e "tags" dopo ogni test
        connection.prepareStatement("DELETE FROM lessons").executeUpdate();
        connection.prepareStatement("DELETE FROM tags").executeUpdate();
        connection.prepareStatement("DELETE FROM tutors").executeUpdate();
        connection.prepareStatement("DELETE FROM students").executeUpdate();
        connection.prepareStatement("DELETE FROM lessonsTags").executeUpdate();
    }

    @Test
    void testGetLessonByID() throws Exception {
        Lesson lesson = lessonsController.getLesson(1);
        assertNotNull(lesson);
        assertEquals("Math Lesson", lesson.getTitle());
        assertEquals("Math basics", lesson.getDescription());
        assertEquals(50.0, lesson.getPrice());
        assertEquals("Available", lesson.getState());
    }

    @Test
    void testGetLessonByIDNonExistent() throws Exception {
        Lesson lesson = lessonsController.getLesson(3);
        assertNull(lesson);
    }

    @Test
    void testGetAllLessons() throws Exception {
        List<Lesson> lessons = lessonsController.getAll();
        assertEquals(2, lessons.size());
    }

    @Test
    void testInsertLesson() throws Exception {
        List<Tag> tags = new ArrayList<>();
        TagSubject ts = new TagSubject("English");
        tags.add(ts);
        int lessonId = lessonsController.addLesson("English Lesson", "English basics", LocalDateTime.now(), LocalDateTime.now(), 40.0, "tutor2", tags);

        // Assicurati che lessonId sia un valore valido
        assertTrue(lessonId > 0);

        // Assicurati che la lezione sia stata inserita correttamente nel database
        Lesson insertedLesson = lessonsController.getLesson(lessonId);
        assertNotNull(insertedLesson);
    }

    @Test
    void testUpdateLesson() throws Exception {
        lessonsController.updateLesson(1, "Updated Math Lesson", "Updated Math basics", LocalDateTime.now(), LocalDateTime.now(), 55.0, "tutor1");
        Lesson updatedLesson = lessonsController.getLesson(1);
        assertEquals("Updated Math Lesson", updatedLesson.getTitle());
        assertEquals("Updated Math basics", updatedLesson.getDescription());
        assertEquals(55.0, updatedLesson.getPrice());
    }

    @Test
    void testDeleteLesson() throws Exception {
        assertTrue(lessonsController.removeLesson(1));
        assertNull(lessonDAO.get(1));
    }

    @Test
    void testDeleteNonExistentLesson() throws Exception {
        assertFalse(lessonsController.removeLesson(99));
    }

    @Test
    void testGetTutorLessonsByState() throws Exception {
        List<Lesson> lessons = lessonsController.getTutorLessonByState("tutor2", new Booked("student1"));
        assertEquals(1, lessons.size());
        assertEquals("Physics Lesson", lessons.get(0).getTitle());
        assertEquals("Booked", lessons.get(0).getState());
    }

    @Test
    void testGetStudentBookedLessons() throws Exception {
        List<Lesson> lessons = lessonDAO.getStudentBookedLessons("student1");
        assertEquals(1, lessons.size());
        assertEquals("Physics Lesson", lessons.get(0).getTitle());
        assertEquals("Booked", lessons.get(0).getState());
    }

    @Test
    void testAttachTagToLesson() throws Exception {
        // Prepara il database di test con una lezione esistente
        TagZone tz = new TagZone("Firenze");
        TagIsOnline tio = new TagIsOnline("True");
        List<Tag> lessonTags = new ArrayList<>();
        lessonTags.add(tz);
        lessonTags.add(tio);
        int idLesson = lessonsController.addLesson("Math Lesson", "Math basics", LocalDateTime.now(), LocalDateTime.now(), 50.0, "tutor1", lessonTags);

        // Crea un nuovo tag
        TagSubject tagSubjectMath = new TagSubject("Math");

        // Aggiungi il tag alla lezione
        lessonsController.attachTag(idLesson, tagSubjectMath);

        // Verifica che il tag sia stato aggiunto correttamente alla lezione
        List<Tag> tags = tagDAO.getTagsByLesson(idLesson);
        assertEquals(3,tags.size());
    }

    @Test
    void testDetachTagFromLesson() throws Exception {
        // Prepara il database di test con una lezione esistente
        TagZone tz = new TagZone("Prato");
        TagIsOnline tio = new TagIsOnline("False");
        tagsController.createTag("Prato", "Zone");
        tagsController.createTag("False", "IsOnline");

        List<Tag> lessonTags = new ArrayList<>();
        lessonTags.add(tz);
        lessonTags.add(tio);
        int idLesson = lessonsController.addLesson("Math Lesson", "Math basics", LocalDateTime.now(), LocalDateTime.now(), 50.0, "tutor1", lessonTags);
        TagSubject tagSubjectMath = new TagSubject("Math");
        lessonsController.attachTag(idLesson, tagSubjectMath);

        List<Tag> tagsPre = tagDAO.getTagsByLesson(idLesson);
        assertEquals(3, tagsPre.size());

        // Rimuovi il tag dalla lezione
        boolean result = lessonsController.detachTag(idLesson, tagSubjectMath);

        // Verifica che il tag sia stato rimosso correttamente dalla lezione
        assertTrue(result);
        List<Tag> tagsPost = tagDAO.getTagsByLesson(idLesson);
        assertEquals(2, tagsPost.size());
    }

    @Test
    void testDetachNonExistentTagFromLesson() throws Exception {
        // Prepara il database di test con una lezione
        List<Tag> tags = new ArrayList<>();
        int lessonId = lessonsController.addLesson("Math Lesson", "Math basics", LocalDateTime.now(), LocalDateTime.now(), 50.0, "tutor1", tags);

        // Crea un tag, ma non lo aggiunge alla lezione

        Tag tag = new TagSubject("TestTag");

        // Prova a rimuovere il tag dalla lezione
        boolean result = lessonsController.detachTag(lessonId, tag);

        // Verifica che il risultato sia false in quanto il tag non era associato alla lezione
        assertFalse(result);
    }
}
