package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.implementation.StudentSchoolServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping("students")
public class StudentController {
    private final StudentSchoolServiceImpl schoolService;

    public StudentController(StudentSchoolServiceImpl schoolService) {
        this.schoolService = schoolService;
    }

    // ---------------- GET REQUESts ----------- //
    @GetMapping("{id}")
    @Operation(summary = "Получить студента по id")
    public ResponseEntity<Student> get(@PathVariable("id") long id) {
        Student student = schoolService.get(id);
        return ResponseEntity.ok(student);

    }

    @GetMapping("/print-parallel")
    @Operation(summary = "Вывести в консоль имена всех студентов в параллельном режиме")
    public ResponseEntity<String> printStudentsParallel() {
        schoolService.printParallel();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/print-synchronized")
    @Operation(summary = "Вывести в консоль имена всех студентов в синхронном режиме")
    public ResponseEntity<String> printStudentsSynchronized() {
        schoolService.printSynchronized();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/faculty")
    @Operation(summary = "Получить факутльтет студента")
    public ResponseEntity<Faculty> getStudentsFaculty(@PathVariable("id") long id) {
        Faculty faculty = schoolService.getStudentsFaculty(id);
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(faculty);

    }

    @GetMapping("/age/{age}")
    @Operation(summary = "Получить всех студентов определенного возраста")
    public ResponseEntity<Collection<Student>> filterByAge(@PathVariable("age") Integer age) {
        Collection<Student> result = schoolService.filterByAge(age);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

    @GetMapping("/age/between/{min}&{max}")
    @Operation(summary = "Получить всех студентов возрастом между min и max")
    public ResponseEntity<Collection<Student>> findAgeByAvearega(@PathVariable("min") Integer min, @PathVariable("max") Integer max) {
        Collection<Student> result = schoolService.findByAgeBetween(min, max);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

    @GetMapping("/count")
    @Operation(summary = "Количество всех студентов в школе")
    public ResponseEntity<Integer> getCountStudents() {
        Integer countStudents = schoolService.getCountStudents();
        if (countStudents == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(countStudents);
    }

    @GetMapping("/average-age")
    @Operation(summary = "Средний возраст студентов университета")
    public ResponseEntity<Integer> getaverageAgeStudents() {
        Integer average = schoolService.getaverageAgeStudents();
        if (average == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(average);
    }

    @GetMapping("/last-five")
    @Operation(summary = "Получить 5 последний студентов")
    public ResponseEntity<Collection<Student>> getLastFiveStudents() {
        Collection<Student> result = schoolService.getLastFiveStudents();
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    // ---------------- POST REQUESts ----------- //
    @PostMapping
    @Operation(summary = "Создать нового студента")
    public Student createUser(@RequestBody Student student) {
        return schoolService.create(student);
    }

    // ---------------- PUT REQUESts ----------- //
    @PutMapping
    @Operation(summary = "Обновить данные студента")
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student result = schoolService.update(student);
        return ResponseEntity.ok(result);
    }

    // ---------------- DELEtE REQUESts ----------- //
    @DeleteMapping("{id}")
    @Operation(summary = "Удалить студента по id")
    public ResponseEntity<Student> deleteStudent(@PathVariable("id") long id) {
        schoolService.remove(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
