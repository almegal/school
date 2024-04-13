package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.implementation.StudentSchoolServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StudentSchoolServiceImplTest {
    private final StudentSchoolServiceImpl schoolServiceImpl = new StudentSchoolServiceImpl();
    private final Map<Long, Student> DEFAULT_MAPS_OF_STUDENT = new HashMap<>();

    @BeforeEach
    void set_up() {
        DEFAULT_MAPS_OF_STUDENT.put((long) 1, new Student(1, 18, "Harry"));
        DEFAULT_MAPS_OF_STUDENT.put((long) 2, new Student(2, 20, "Ron"));
        DEFAULT_MAPS_OF_STUDENT.put((long) 3, new Student(3, 18, "Hermiona"));
        DEFAULT_MAPS_OF_STUDENT.put((long) 4, new Student(4, 20, "Luna"));

        schoolServiceImpl.create(DEFAULT_MAPS_OF_STUDENT.get((long) 1));
    }


    @Test
    @DisplayName("Создание  студента")
    public void createFaculty() {
        // подготовка ожидаемого результата
        Student expected = DEFAULT_MAPS_OF_STUDENT.get((long) 1);
        // подготовка актуального результата
        Student actual = schoolServiceImpl.create(expected);
        // тест
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Удаление студента")
    public void removeFacyltu() {
        // подготовка ожидаемого результата
        Student expected = DEFAULT_MAPS_OF_STUDENT.get((long) 1);
        // подготовка актуального результата
        Student actual = schoolServiceImpl.remove(1);
        // тест
        assertEquals(expected, actual);
        assertNull(schoolServiceImpl.get(1));
    }

    @Test
    @DisplayName("Получить студента по индексу")
    public void getFacultyById() {
        // подготовка ожидаемого результата
        Student expected = DEFAULT_MAPS_OF_STUDENT.get((long) 1);
        // подготовка актуального результата
        Student actual = schoolServiceImpl.get(1);
        // тест
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Редактировать студента")
    public void editFaculty() {
        // подготовка ожидаемого результата
        Student expected = new Student(1, 100, "Brown");
        // подготовка актуального результата
        Student actual = schoolServiceImpl.update(expected);
        // тест
        assertEquals(expected, actual);
        assertEquals(expected, schoolServiceImpl.get(1));
    }

    @Test
    @DisplayName("Получить студентов по возрасту")
    public void getFacultysByColor() {
        // подготовка ожидаемого результата
        Map<Long, Student> expected = new HashMap<>();
        Student student = DEFAULT_MAPS_OF_STUDENT.get((long) 1);
        expected.put((long) 1, student);
        // подготовка актуального результата
        Map<Long, Student> actual = schoolServiceImpl.byFilter(18);
        // тест
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Исключение если фильтрация не по целочисленному аргументу")
    public void thrownErrorIfIlliegalArgsInFilterMethod() {
        String arg = "1";
        String errorMsg = String.format("метод byFilter ожидает int. Передан %s", arg.getClass());
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> schoolServiceImpl.byFilter(arg));
        assertEquals(errorMsg, thrown.getMessage());
    }
}
