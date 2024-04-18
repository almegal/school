package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentsRepository;
import ru.hogwarts.school.service.implementation.StudentSchoolServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentSchoolServiceImplTest {
    private final Map<Long, Student> DEFAULT_MAPS_OF_STUDENT = new HashMap<>();
    @Mock
    private StudentsRepository studentsRepository;
    @InjectMocks
    private StudentSchoolServiceImpl schoolServiceImpl;

    @BeforeEach
    void set_up() {
        DEFAULT_MAPS_OF_STUDENT.put((long) 1, new Student(1, 18, "Harry"));
        DEFAULT_MAPS_OF_STUDENT.put((long) 2, new Student(2, 20, "Ron"));
        DEFAULT_MAPS_OF_STUDENT.put((long) 3, new Student(3, 18, "Hermiona"));
        DEFAULT_MAPS_OF_STUDENT.put((long) 4, new Student(4, 20, "Luna"));
    }


    @Test
    @DisplayName("Создание  студента")
    public void createFaculty() {
        // подготовка ожидаемого результата
        Student expected = DEFAULT_MAPS_OF_STUDENT.get((long) 1);
        when(studentsRepository.save(any(Student.class))).thenReturn(expected);
        // подготовка актуального результата
        Student actual = schoolServiceImpl.create(expected);
        // тест
        assertEquals(expected, actual);
        verify(studentsRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Удаление студента")
    public void removeFacyltu() {
        // подготовка ожидаемого результата
        Student expected = DEFAULT_MAPS_OF_STUDENT.get((long) 1);
        when(studentsRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(studentsRepository).deleteById(anyLong());
        // подготовка актуального результата
        schoolServiceImpl.remove(anyLong());
        // тест
        verify(studentsRepository, times(1)).existsById(anyLong());
        verify(studentsRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Исключение при удалении если нет такого студента")
    public void throwWhenTryRemoveFacultyIfNotExsist() {
        // подготовка ожидаесого результата
        Student expected = DEFAULT_MAPS_OF_STUDENT.get((long) 1);
        when(studentsRepository.existsById(anyLong())).thenReturn(false);
        // подготовка актуального результата
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> schoolServiceImpl.remove(anyLong()));
        // тест
        assertEquals("Такого студента нет в БД", thrown.getMessage());
        verify(studentsRepository, times(1)).existsById(anyLong());
        verify(studentsRepository, times(0)).deleteById(anyLong());
    }


    @Test
    @DisplayName("Получить студента по индексу")
    public void getFacultyById() {
        // подготовка ожидаемого результата
        Student expected = DEFAULT_MAPS_OF_STUDENT.get((long) 1);
        when(studentsRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        // подготовка актуального результата
        Student actual = schoolServiceImpl.get(1);
        // тест
        assertEquals(expected, actual);
        verify(studentsRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Редактировать студента")
    public void editFaculty() {
        // подготовка ожидаемого результата
        Student expected = new Student(1, 19, "Brown");
        when(studentsRepository.existsById(anyLong())).thenReturn(true);
        when(studentsRepository.save(any(Student.class))).thenReturn(expected);
        // подготовка актуального результата
        Student actual = schoolServiceImpl.update(new Student(1, 19, "Brown"));
        // тест
        assertEquals(expected, actual);
        verify(studentsRepository, times(1)).save(any(Student.class));
        verify(studentsRepository, times(1)).existsById(anyLong());

    }
    @Test
    @DisplayName("Исключение при редактировании если нет такого студента")
    public void throwWhenTryUpdateFacultyIfNotExsist() {
        // подготовка ожидаесого результата
        Student expected = DEFAULT_MAPS_OF_STUDENT.get((long) 1);
        when(studentsRepository.existsById(anyLong())).thenReturn(false);
        // подготовка актуального результата
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> schoolServiceImpl.update(expected));
        // тест
        assertEquals("Такого студента нет в БД", thrown.getMessage());
        verify(studentsRepository, times(1)).existsById(anyLong());
        verify(studentsRepository, times(0)).save(any(Student.class));
    }

    @Test
    @DisplayName("Получить студентов по возрасту")
    public void getFacultysByColor() {
//        // подготовка ожидаемого результата
//        Map<Long, Student> expected = new HashMap<>();
//        Student student = DEFAULT_MAPS_OF_STUDENT.get((long) 1);
//        expected.put((long) 1, student);
//        // подготовка актуального результата
//        Map<Long, Student> actual = schoolServiceImpl.byFilter(18);
//        // тест
//        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Получить студентов по возрасту")
    public void getCollectionsFacultysByColor() {
        // подготовка ожидаемого результата
        Collection<Student> expected = new ArrayList<>();
        expected.add(DEFAULT_MAPS_OF_STUDENT.get((long) 2));
        expected.add(DEFAULT_MAPS_OF_STUDENT.get((long) 4));
        when(studentsRepository.findAllByAge(20)).thenReturn(expected);
        // подготовка актуального результата
        Collection<Student> actual = schoolServiceImpl.filterByAge(20);
        // тест
        assertEquals(expected, actual);
        verify(studentsRepository, times(1)).findAllByAge(20);
    }
}
