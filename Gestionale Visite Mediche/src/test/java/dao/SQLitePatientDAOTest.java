package test.java.dao;

import domainModel.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class SQLitePatientDAOTest {
    private SQLiteStudentDAO studentDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the test database
        Database.setDatabase("test.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws SQLException {
        Connection connection = Database.getConnection();
        studentDAO = new SQLiteStudentDAO();

        // Clear the "students" table
        connection.prepareStatement("DELETE FROM students").executeUpdate();
        // Insert some test data
        connection.prepareStatement("INSERT INTO students (cf, name, surname, level) VALUES ('test1', 'name1', 'surname1', 'level1')").executeUpdate();
        connection.prepareStatement("INSERT INTO students (cf, name, surname, level) VALUES ('test2', 'name2', 'surname2', 'level2')").executeUpdate();
    }

    @Test
    void testGetStudentByCF() throws SQLException {
        // Test the get method to retrieve a student by CF
        Student student = studentDAO.get("test1");
        Assertions.assertNotNull(student);
        Assertions.assertEquals("test1", student.getCF());
        Assertions.assertEquals("name1", student.getName());
        Assertions.assertEquals("surname1", student.getSurname());
        Assertions.assertEquals("level1", student.getLevel());
    }

    @Test
    void testGetStudentByCFNonExistent() throws SQLException {
        // Test the get method with a non-existent CF
        Student student = studentDAO.get("nonexistent");
        Assertions.assertNull(student);
    }

    @Test
    void testGetAllStudents() throws SQLException {
        // Test the getAll method to retrieve all students
        List<Student> students = studentDAO.getAll();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    void testAddStudent() throws SQLException {
        // Test the insert method to add a new student
        Student student = new Student("test3", "name3", "surname3", "level3");
        studentDAO.insert(student);
        List<Student> students = studentDAO.getAll();
        Assertions.assertEquals(3, students.size());
    }

    @Test
    void testUpdateStudent() throws SQLException {
        // Test the update method to update a student's information
        Student student = new Student("test1", "name1_updated", "surname1_updated", "level1_updated");
        studentDAO.update(student);
        Student updatedStudent = studentDAO.get("test1");
        Assertions.assertEquals("name1_updated", updatedStudent.getName());
        Assertions.assertEquals("surname1_updated", updatedStudent.getSurname());
        Assertions.assertEquals("level1_updated", updatedStudent.getLevel());
    }

    @Test
    void testDeleteStudent() throws SQLException {
        // Test the delete method to delete a student
        boolean result = studentDAO.delete("test1");
        Assertions.assertTrue(result);
        List<Student> students = studentDAO.getAll();
        Assertions.assertEquals(1, students.size());
    }

    @Test
    void testDeleteNonExistentStudent() throws SQLException {
        // Test the delete method with a non-existent student
        boolean result = studentDAO.delete("nonexistent");
        Assertions.assertFalse(result);
        List<Student> students = studentDAO.getAll();
        Assertions.assertEquals(2, students.size());
    }
}
