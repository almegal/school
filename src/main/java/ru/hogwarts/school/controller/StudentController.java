package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.SchoolService;

import java.util.Map;

@RestController
@RequestMapping("students")
public class StudentController {
    private final SchoolService<Student> schoolService;

    public StudentController(@Qualifier("StudentSchoolServiceImpl") SchoolService<Student> schoolService) {
        this.schoolService = schoolService;
    }


    @GetMapping("{id}")
    public ResponseEntity<Student> get(@PathVariable("id") long id) {
        Student student = schoolService.get(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(student);

    }

    @GetMapping("/age/{age}")
    public ResponseEntity<Map<Long, Student>> filterByColor(@PathVariable("age") Integer age) {
        Map<Long, Student> result = schoolService.byFilter(age);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

    @PostMapping
    public Student createUser(Student student) {
        return schoolService.create(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(Student student) {
        Student editStudent = schoolService.update(student);
        if (editStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable("id") long id) {
        Student student = schoolService.remove(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);
    }
}
