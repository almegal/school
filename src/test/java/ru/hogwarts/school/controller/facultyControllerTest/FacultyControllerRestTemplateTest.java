package ru.hogwarts.school.controller.facultyControllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.implementation.FacultySchoolServiceImpl;
import ru.hogwarts.school.service.implementation.StudentSchoolServiceImpl;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.hogwarts.school.controller.ConfigForControllerTest.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerRestTemplateTest {
    @LocalServerPort
    private int port;
    @Autowired
    private FacultyController controller;
    @Autowired
    private FacultySchoolServiceImpl facultySchoolService;
    @Autowired
    private StudentSchoolServiceImpl studentSchoolService;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void set_up() {
        // перед каждым тестом добавим тестовые данные
        FACULTIES_COLLECTION.forEach(f -> {
            facultySchoolService.create(f);
        });
    }

    @Test
    @DisplayName("FacultyController загружен")
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    //Очистить данные в БД перед тестом
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L})
    @DisplayName("Получить факультет по id")
    public void getControllerTest(long id) {
        // подготовка актуального результата
        ResponseEntity<Faculty> response = this.testRestTemplate.getForEntity(
                HOST + port + String.format("/facultys/%d", id),
                Faculty.class
        );
        Faculty actual = response.getBody();
        // подготовка ожидаемого результата
        Faculty expected = IntStream
                // создадим числовой стрим размером дефолтной коллекции факультетов
                .range(0, FACULTIES_COLLECTION.size())
                // найдем только тот индекс который равен текущему id
                .filter(i -> i + 1 == id)
                // преоброзуем числовой стрим в стрим объектов
                .mapToObj(FACULTIES_COLLECTION::get)
                // получим факультет который соответствовал условию
                .findFirst()
                // или вернем новый
                .orElse(new Faculty("oops", " boops"));
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getColor()).isEqualTo(expected.getColor());
    }

    @Test
    @DisplayName("Post факультета")
    public void postControllerTest() throws Exception {
        // подготовка актуального результата
        ResponseEntity<Faculty> response = this.testRestTemplate.postForEntity(
                HOST + port + "/facultys",
                FACULTY_DEFAULT,
                Faculty.class
        );
        Faculty actual = response.getBody();
        // тесты
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(FACULTY_DEFAULT.getColor()).isEqualTo(actual.getColor());
        assertThat(FACULTY_DEFAULT.getName()).isEqualTo(actual.getName());
    }

    //Очистить данные в БД перед тестом
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    @DisplayName("Обновление факультета")
    public void putControllerTest() {
        // подготовка ожидаемого значения
        Faculty expected = new Faculty();
        expected.setId(1);
        expected.setName("Test");
        expected.setColor("Green");
        // подготовка актуального результата
        this.testRestTemplate.put(HOST + port + "/facultys", expected, Faculty.class);
        ResponseEntity<Faculty> response = this.testRestTemplate.getForEntity(
                HOST + port + "/facultys/1",
                Faculty.class
        );
        Faculty actual = response.getBody();
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isEqualTo(expected);
    }

    //Очистить данные в БД перед тестом
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L})
    @DisplayName("Удаление факультета")
    public void deleteControllerTest(long id) {
        // подготовка актуального результата
        this.testRestTemplate.delete(HOST + port + String.format("/facultys/%d", id), Faculty.class);
        ResponseEntity<Faculty> response = this.testRestTemplate.getForEntity(
                HOST + port + String.format("/facultys/%d", id),
                Faculty.class
        );
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Получить всех студентов факультета")
    public void getStudentsInFacultyControllerTest() {
        // подготовка актуального результата
        STUDENT_DEFAULT.setFaculty(FACULTIES_COLLECTION.get(0));
        studentSchoolService.create(STUDENT_DEFAULT);
        ResponseEntity<Set<Student>> response = this.testRestTemplate.exchange(
                HOST + port + "/facultys/1/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Set<Student>>() {
                });
        Set<Student> actual = response.getBody();
        // подготовка ожидаемого значения
        Set<Student> expected = Set.of(STUDENT_DEFAULT);
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Red", "blue", "GrEEn"})
    @DisplayName("Получить факультет по Цвету")
    public void getByColorControllerTest(String color) {
        // подготовка актуального результата
        ResponseEntity<Collection<Faculty>> response = this.testRestTemplate.exchange(
                HOST + port + String.format("/facultys/color/%s", color),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {
                });
        Collection<Faculty> actual = response.getBody();
        // подготовка ожидаемого значения
        Collection<Faculty> expected = FACULTIES_COLLECTION.stream()
                .filter(f -> f.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Greenmond", "bLacKmond", "Redmond"})
    @DisplayName("Получить факультет по имени")
    public void getByNameControllerTest(String name) {
        // подготовка актуального результата
        ResponseEntity<Faculty> response = this.testRestTemplate.getForEntity(
                HOST + port + String.format("/facultys/name/%s", name),
                Faculty.class
        );
        Faculty actual = response.getBody();
        // подготовка ожидаемого значения
        Faculty expected = FACULTIES_COLLECTION.stream()
                .filter(f -> f.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(new Faculty());
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isEqualTo(expected);
    }
}
