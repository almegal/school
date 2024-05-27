package ru.hogwarts.school.controller.studentControllerTest;

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
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentsRepository;
import ru.hogwarts.school.service.implementation.StudentSchoolServiceImpl;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.hogwarts.school.controller.ConfigForControllerTest.*;

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerMvcTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentsRepository repository;
    @SpyBean
    private StudentSchoolServiceImpl service;
    @InjectMocks
    private StudentController controller;

    @Test
    @DisplayName("StudentController загружен")
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("Получить студента по id")
    public void getController() throws Exception {
        // подготовка актуального результата
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(STUDENT_DEFAULT));
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/students/" + 1)
                        .accept(MediaType.APPLICATION_JSON));
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(STUDENT_NAME))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE));
        verify(repository, times(1)).findById(any(Long.class));
        verify(service, times(1)).get(any(Long.class));
    }

    @Test
    @DisplayName("Post студента")
    public void postController() throws Exception {
        // подготовка ожидаемого результата
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("age", STUDENT_AGE);
        jsonObject.put("name", STUDENT_NAME);

        // подготовка актуального результата
        when(repository.save(any(Student.class))).thenReturn(STUDENT_DEFAULT);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/students")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(STUDENT_NAME))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE));
        verify(repository, times(1)).save(any(Student.class));
        verify(service, times(1)).create(any(Student.class));
    }

    @Test
    @DisplayName("Обновление студента")
    public void updateController() throws Exception {
        // подготовка ожидаемого результата
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", STUDENT_NAME);
        jsonObject.put("age", STUDENT_AGE);
        jsonObject.put("id", 1);
        Student expected = new Student();
        expected.setName(STUDENT_NAME);
        expected.setId(1);
        expected.setAge(STUDENT_AGE);
        // подготовка актуального резульата
        when(repository.existsById(any(Long.class))).thenReturn(true);
        when(repository.save(expected)).thenReturn(expected);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/students")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(STUDENT_NAME))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE));
        verify(repository, times(1)).existsById(any(Long.class));
        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Удалить студента")
    public void deleteController() throws Exception {
        // подготовка актуального результат
        when(repository.existsById(any(Long.class))).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());
        // подготовка ожидаемого значение
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/students/1")
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
    @DisplayName("Получить всех студентов определенного возраста")
    public void getByAgeController() throws Exception {
        // подготовка ожидаемого результата
        Collection<Student> expected = STUDENT_COLLECTION.stream()
                .filter(f -> f.getAge() == 20)
                .toList();
        // подготовка актуального результат
        when(repository.findAllByAge(any(Integer.class))).thenReturn(expected);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/students/age/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        ;
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
        verify(repository, times(1)).findAllByAge(any(Integer.class));
    }

    @Test
    @DisplayName("Получить всех студентов возрастом между min и max")
    public void getStudentsInFacultyController() throws Exception {
        // подготовка ожидаемого результата
        Collection<Student> expected = STUDENT_COLLECTION.stream()
                .filter(f -> f.getAge() >= 20 && f.getAge() <= 49)
                .toList();
        // подготовка актуального результат
        when(repository.findByAgeBetween(any(Integer.class), any(Integer.class))).thenReturn(expected);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/students/age/between/20&49")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
        verify(repository, times(1)).findByAgeBetween(any(Integer.class), any(Integer.class));
    }

    @Test
    @DisplayName("Получить факультет студента")
    public void getStudentsFacultyControllerTest() throws Exception {
        // подготовка ожидаемого результата
        Student expectedStudent = new Student();
        expectedStudent.setId(1L);
        expectedStudent.setFaculty(FACULTY_DEFAULT);
        // подготовка актуального результат
        when(repository.findById(anyLong())).thenReturn(Optional.of(expectedStudent));
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/students/1/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // тест
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
        verify(repository, times(1)).findById(anyLong());
        verify(service, times(1)).getStudentsFaculty(anyLong());
    }
}
