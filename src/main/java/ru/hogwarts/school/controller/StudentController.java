package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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


    @GetMapping("{id}")
    public ResponseEntity<Student> get(@PathVariable("id") long id) {
        Student student = schoolService.get(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(student);

    }

    @GetMapping("/age/{age}")
    public ResponseEntity<Collection<Student>> filterByAge(@PathVariable("age") Integer age) {
        Collection<Student> result = schoolService.filterByAge(age);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

    @GetMapping("/age/between/{min}&{max}")
    public ResponseEntity<Collection<Student>> findAgeByAvearega(
            @PathVariable("min") Integer min,
            @PathVariable("max") Integer max) {
        Collection<Student> result = schoolService.findByAgeBetween(min, max);
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
        Student result;
        try {
            result = schoolService.update(student);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable("id") long id) {
        try {
            schoolService.remove(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
