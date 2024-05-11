package ru.hogwarts.school.controller.studentControllerTest;

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
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.implementation.FacultySchoolServiceImpl;
import ru.hogwarts.school.service.implementation.StudentSchoolServiceImpl;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.hogwarts.school.controller.ConfigForControllerTest.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerRestTemplateTest {
    private final String HOST = "http://localhost:";
    @LocalServerPort
    private int port;
    @Autowired
    private StudentController controller;
    @Autowired
    private FacultySchoolServiceImpl facultySchoolService;
    @Autowired
    private StudentSchoolServiceImpl studentSchoolService;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void set_up() {
        // перед каждым тестом добавим тестовые данные
        STUDENT_COLLECTION.forEach(s -> {
            studentSchoolService.create(s);
        });
    }

    @Test
    @DisplayName("StudentController загружен")
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    //Очистить данные в БД перед тестом
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L})
    @DisplayName("Получить студента по id")
    public void getControllerTest(long id) {
        // подготовка актуального результата
        ResponseEntity<Student> response = this.testRestTemplate.getForEntity(
                HOST + port + String.format("/students/%d", id),
                Student.class
        );
        Student actual = response.getBody();
        // подготовка ожидаемого результата
        Student expected = IntStream
                // создадим числовой стрим размером дефолтной коллекции студентов
                .range(0, STUDENT_COLLECTION.size())
                // найдем только тот индекс который равен текущему id
                .filter(i -> i + 1 == id)
                // преоброзуем числовой стрим в стрим объектов
                .mapToObj(STUDENT_COLLECTION::get)
                // получим стуеднта который соответствовал условию
                .findFirst()
                // или вернем новый
                .orElse(new Student(1, " boops"));
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
    }

    @Test
    @DisplayName("Post студента")
    public void postControllerTest() throws Exception {
        // подготовка актуального результата
        ResponseEntity<Student> response = this.testRestTemplate.postForEntity(
                HOST + port + "/students",
                STUDENT_DEFAULT,
                Student.class
        );
        Student actual = response.getBody();
        // тесты
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(STUDENT_DEFAULT.getAge()).isEqualTo(actual.getAge());
        assertThat(STUDENT_DEFAULT.getName()).isEqualTo(actual.getName());
    }

    //Очистить данные в БД перед тестом
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    @DisplayName("Обновление cтудента")
    public void putControllerTest() {
        // подготовка ожидаемого значения
        Student expected = new Student();
        expected.setId(1);
        expected.setName("Test");
        expected.setAge(10000);
        // подготовка актуального результата
        this.testRestTemplate.put(HOST + port + "/students", expected, Student.class);
        ResponseEntity<Student> response = this.testRestTemplate.getForEntity(
                HOST + port + "/students/1",
                Student.class
        );
        Student actual = response.getBody();
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L})
    @DisplayName("Удаление студента")
    public void deleteControllerTest(long id) {
        // подготовка актуального результата
        this.testRestTemplate.delete(HOST + port + String.format("/students/%d", id), Student.class);
        ResponseEntity<Student> response = this.testRestTemplate.getForEntity(
                HOST + port + String.format("/students/%d", id),
                Student.class
        );
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Получить факультет студента")
    public void getStudentsFacultyControllerTest() {
        // подготовка ожидаемого значения
        Faculty expected = FACULTIES_COLLECTION.get(0);
        // подготовка актуального результата
        // создадим факультет в БД
        facultySchoolService.create(expected);
        // установим созданный факультет для нашего студента
        STUDENT_DEFAULT.setFaculty(expected);
        STUDENT_DEFAULT.setId(1L);
        // сохраняем в БД
        studentSchoolService.create(STUDENT_DEFAULT);
        ResponseEntity<Faculty> response = this.testRestTemplate.getForEntity(
                HOST + port + "/students/1/faculty",
                Faculty.class);
        Faculty actual = response.getBody();
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Получить студентов определенного возраста")
    public void getByAgeControllerTest() {
        // подготовка актуального результата
        ResponseEntity<Collection<Student>> response = this.testRestTemplate.exchange(
                HOST + port + "/students/age/20",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                });
        Collection<Student> actual = response.getBody();
        // подготовка ожидаемого значения
        Collection<Student> expected = STUDENT_COLLECTION.stream()
                .filter(s -> s.getAge() == 20)
                .collect(Collectors.toList());
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Получить студентов по диапозону возраста")
    public void getByAvearageAgeControllerTest() {
        // подготовка актуального результата
        ResponseEntity<Collection<Student>> response = this.testRestTemplate.exchange(
                HOST + port + "/students/age/between/20&40",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                });
        Collection<Student> actual = response.getBody();
        // подготовка ожидаемого значения
        Collection<Student> expected = STUDENT_COLLECTION.stream()
                .filter(s -> s.getAge() >= 20 && s.getAge() <= 49)
                .collect(Collectors.toSet());
        // тест
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).hasSameElementsAs(expected);
    }
}
