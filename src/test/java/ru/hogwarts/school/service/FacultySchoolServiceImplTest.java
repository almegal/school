package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultysRepository;
import ru.hogwarts.school.service.implementation.FacultySchoolServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacultySchoolServiceImplTest {
    private final Map<Long, Faculty> DEFAULT_MAPS_OF_FACULTY = new HashMap<>();
    @Mock
    private FacultysRepository facultysRepository;
    @InjectMocks
    private FacultySchoolServiceImpl schoolServiceImpl;

    @BeforeEach
    void set_up() {
        DEFAULT_MAPS_OF_FACULTY.put((long) 1, new Faculty(1, "Griffindor", "Red"));
        DEFAULT_MAPS_OF_FACULTY.put((long) 2, new Faculty(2, "Slytherin", "Red"));
        DEFAULT_MAPS_OF_FACULTY.put((long) 3, new Faculty(3, "Hufflepufe", "Yellow"));
        DEFAULT_MAPS_OF_FACULTY.put((long) 4, new Faculty(4, "Ravenclan", "Blue"));
    }


    @Test
    @DisplayName("Создание факультетат")
    public void createFaculty() {
        // подготовка ожидаемого результата
        Faculty expected = DEFAULT_MAPS_OF_FACULTY.get((long) 1);
        when(facultysRepository.save(any(Faculty.class))).thenReturn(expected);
        // подготовка актуального результата
        Faculty actual = schoolServiceImpl.create(expected);
        // тест
        assertEquals(expected, actual);
        verify(facultysRepository, times(1)).save(any(Faculty.class));
    }

    @Test
    @DisplayName("Удаление факультета")
    public void removeFacyltu() {
        // подготовка ожидаемого результата
        Faculty expected = DEFAULT_MAPS_OF_FACULTY.get((long) 1);
        when(facultysRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(facultysRepository).deleteById(anyLong());
        // подготовка актуального результата
        schoolServiceImpl.remove(anyLong());
        // тест
        verify(facultysRepository, times(1)).existsById(anyLong());
        verify(facultysRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Исключение при удалении если нет такого факультета")
    public void throwWhenTryRemoveFacultyIfNotExsist() {
        // подготовка ожидаесого результата
        Faculty expected = DEFAULT_MAPS_OF_FACULTY.get((long) 1);
        when(facultysRepository.existsById(anyLong())).thenReturn(false);
        // подготовка актуального результата
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> schoolServiceImpl.remove(anyLong()));
        // тест
        assertEquals("Такого факультута нет в БД", thrown.getMessage());
        verify(facultysRepository, times(1)).existsById(anyLong());
        verify(facultysRepository, times(0)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Получить факультет по индексу")
    public void getFacultyById() {
        // подготовка ожидаемого результата
        Faculty expected = DEFAULT_MAPS_OF_FACULTY.get((long) 1);
        when(facultysRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        // подготовка актуального результата
        Faculty actual = schoolServiceImpl.get(1);
        // тест
        assertEquals(expected, actual);
        verify(facultysRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Редактировать факультет")
    public void editFaculty() {
        // подготовка ожидаемого результата
        Faculty expected = new Faculty(1, "No Griffindor", "Brown");
        when(facultysRepository.existsById(anyLong())).thenReturn(true);
        when(facultysRepository.save(any(Faculty.class))).thenReturn(expected);
        // подготовка актуального результата
        Faculty actual = schoolServiceImpl.update(new Faculty(1, "No Griffindor", "Brown"));
        // тест
        assertEquals(expected, actual);
        verify(facultysRepository, times(1)).save(any(Faculty.class));
        verify(facultysRepository, times(1)).existsById(anyLong());

    }

    @Test
    @DisplayName("Исключение при редактировании если нет такого факультета")
    public void throwWhenTryUpdateFacultyIfNotExsist() {
        // подготовка ожидаесого результата
        Faculty expected = DEFAULT_MAPS_OF_FACULTY.get((long) 1);
        when(facultysRepository.existsById(anyLong())).thenReturn(false);
        // подготовка актуального результата
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> schoolServiceImpl.update(expected));
        // тест
        assertEquals("Такого факультута нет в БД", thrown.getMessage());
        verify(facultysRepository, times(1)).existsById(anyLong());
        verify(facultysRepository, times(0)).save(any(Faculty.class));
    }

    @Test
    @DisplayName("Получить факультеты по цвету")
    public void getCollectionsFacultysByColor() {
        // подготовка ожидаемого результата
        Collection<Faculty> expected = new ArrayList<>();
        expected.add(DEFAULT_MAPS_OF_FACULTY.get((long) 1));
        expected.add(DEFAULT_MAPS_OF_FACULTY.get((long) 2));
        when(facultysRepository.findAllByColor("Red")).thenReturn(expected);
        // подготовка актуального результата
        Collection<Faculty> actual = schoolServiceImpl.filterByColor("Red");
        // тест
        assertEquals(expected, actual);
        verify(facultysRepository, times(1)).findAllByColor("Red");
    }
}
