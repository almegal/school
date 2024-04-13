package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.implementation.FacultySchoolServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FacultySchoolServiceImplTest {
    private final FacultySchoolServiceImpl schoolServiceImpl = new FacultySchoolServiceImpl();
    private final Map<Long, Faculty> DEFAULT_MAPS_OF_FACULTY = new HashMap<>();

    @BeforeEach
    void set_up() {
        DEFAULT_MAPS_OF_FACULTY.put((long) 1, new Faculty(1, "Griffindor", "Red"));
        DEFAULT_MAPS_OF_FACULTY.put((long) 2, new Faculty(2, "Slytherin", "Green"));
        DEFAULT_MAPS_OF_FACULTY.put((long) 3, new Faculty(3, "Hufflepufe", "Yellow"));
        DEFAULT_MAPS_OF_FACULTY.put((long) 4, new Faculty(4, "Ravenclan", "Blue"));

        schoolServiceImpl.create(DEFAULT_MAPS_OF_FACULTY.get((long) 1));
    }


    @Test
    @DisplayName("Создание факультетат")
    public void createFaculty() {
        // подготовка ожидаемого результата
        Faculty expected = DEFAULT_MAPS_OF_FACULTY.get((long) 1);
        // подготовка актуального результата
        Faculty actual = schoolServiceImpl.create(expected);
        // тест
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Удаление факультета")
    public void removeFacyltu() {
        // подготовка ожидаемого результата
        Faculty expected = DEFAULT_MAPS_OF_FACULTY.get((long) 1);
        // подготовка актуального результата
        Faculty actual = schoolServiceImpl.remove(1);
        // тест
        assertEquals(expected, actual);
        assertNull(schoolServiceImpl.get(1));
    }

    @Test
    @DisplayName("Получить факультет по индексу")
    public void getFacultyById() {
        // подготовка ожидаемого результата
        Faculty expected = DEFAULT_MAPS_OF_FACULTY.get((long) 1);
        // подготовка актуального результата
        Faculty actual = schoolServiceImpl.get(1);
        // тест
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Редактировать факультет")
    public void editFaculty() {
        // подготовка ожидаемого результата
        Faculty expected = new Faculty(1, "No Griffindor", "Brown");
        // подготовка актуального результата
        Faculty actual = schoolServiceImpl.update(expected);
        // тест
        assertEquals(expected, actual);
        assertEquals(expected, schoolServiceImpl.get(1));
    }

    @Test
    @DisplayName("Получить факультеты по цвету")
    public void getFacultysByColor() {
        // подготовка ожидаемого результата
        Map<Long, Faculty> expected = new HashMap<>();
        Faculty faculty = DEFAULT_MAPS_OF_FACULTY.get((long) 1);
        expected.put((long) 1, faculty);
        // подготовка актуального результата
        Map<Long, Faculty> actual = schoolServiceImpl.byFilter("Red");
        // тест
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Исключение если фильтрация не по строке")
    public void thrownErrorIfIlliegalArgsInFilterMethod() {
        Integer arg = 1;
        String errorMsg = String.format("метод byFilter ожидает строку. Передан %s", arg.getClass());
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> schoolServiceImpl.byFilter(arg));
        assertEquals(errorMsg, thrown.getMessage());
    }
    // подготовка ожидаемого результата
    // подготовка актуального результата
    // тест
}
