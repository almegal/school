package ru.hogwarts.school.controller.facultyControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultysRepository;
import ru.hogwarts.school.service.implementation.FacultySchoolServiceImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.hogwarts.school.controller.ConfigForControllerTest.*;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerMvcTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultysRepository repository;
    @SpyBean
    private FacultySchoolServiceImpl service;
    @InjectMocks
    private FacultyController controller;

    @Test
    @DisplayName("FacultyController загружен")
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("Получить факультет по id")
    public void getController() throws Exception {
        // подготовка актуального результата
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY_DEFAULT));
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/facultys/" + 1)
                        .accept(MediaType.APPLICATION_JSON));
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(FACULTY_DEFAULT.getName()))
                .andExpect(jsonPath("$.color").value(FACULTY_DEFAULT.getColor()));
        verify(repository, times(1)).findById(any(Long.class));
        verify(service, times(1)).get(any(Long.class));
    }

    @Test
    @DisplayName("Post факультета")
    public void postController() throws Exception {
        // подготовка ожидаемого результата
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("color", FACULTY_COLOR);
        jsonObject.put("name", FACULTY_NAME);

        // подготовка актуального результата
        when(repository.save(any(Faculty.class))).thenReturn(FACULTY_DEFAULT);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/facultys")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
        verify(repository, times(1)).save(any(Faculty.class));
        verify(service, times(1)).create(any(Faculty.class));
    }

    @Test
    @DisplayName("Обновление факультета")
    public void updateController() throws Exception {
        // подготовка ожидаемого результата
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", FACULTY_NAME);
        jsonObject.put("color", FACULTY_COLOR);
        jsonObject.put("id", 1);
        Faculty expected = new Faculty();
        expected.setName(FACULTY_NAME);
        expected.setId(1);
        expected.setColor(FACULTY_COLOR);
        // подготовка актуального резульата
        when(repository.existsById(any(Long.class))).thenReturn(true);
        when(repository.save(expected)).thenReturn(expected);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/facultys")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
        verify(repository, times(1)).existsById(any(Long.class));
        verify(repository, times(1)).save(any(Faculty.class));
    }

    @Test
    @DisplayName("Удалить факультет")
    public void deleteController() throws Exception {
        // подготовка актуального результат
        when(repository.existsById(any(Long.class))).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());
        // подготовка ожидаемого значение
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/facultys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // тест
        resultActions
                .andExpect(status().isOk());
        verify(repository, times(1)).existsById(any(Long.class));
        verify(repository, times(1)).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Получить факультет по имени")
    public void getByNameController() throws Exception {
        // подготовка актуального результат
        when(repository.findByNameIgnoreCase(any(String.class))).thenReturn(FACULTY_DEFAULT);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/facultys/name/" + FACULTY_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
        verify(repository, times(1)).findByNameIgnoreCase(any(String.class));
    }

    @Test
    @DisplayName("Получить все факультеты по цвету")
    public void getByColorController() throws Exception {
        // подготовка ожидаемого результата
        Collection<Faculty> expected = FACULTIES_COLLECTION.stream()
                .filter(f -> f.getColor().equals("Red"))
                .toList();
        // подготовка актуального результат
        when(repository.findAllByColorIgnoreCase(any(String.class))).thenReturn(expected);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/facultys/color/Red")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        ;
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
        verify(repository, times(1)).findAllByColorIgnoreCase(any(String.class));
    }

    @Test
    @DisplayName("Получить всех студентов факультета")
    public void getStudentsInFacultyController() throws Exception {
        // подготовка ожидаемого результата
        FACULTY_DEFAULT.setStudents(new HashSet<>(STUDENT_COLLECTION));
        // подготовка актуального результат
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY_DEFAULT));
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/facultys/1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(FACULTY_DEFAULT.getStudents())));
        verify(repository, times(1)).findById(any(Long.class));
    }
}
