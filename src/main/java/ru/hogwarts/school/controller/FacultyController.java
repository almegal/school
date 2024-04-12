package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.SchoolService;

import java.util.Map;

@RestController
@RequestMapping("facultys")
public class FacultyController {
    private final SchoolService<Faculty> schoolService;

    public FacultyController(@Qualifier("FacultySchoolServiceImpl") SchoolService<Faculty> schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> get(@PathVariable("id") long id) {
        Faculty faculty = schoolService.get(id);
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(faculty);

    }

    @GetMapping("/color/{color}")
    public ResponseEntity<Map<Long, Faculty>> filterByColor(@PathVariable("color") String color) {
        Map<Long, Faculty> result = schoolService.byFilter(color);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

    @PostMapping
    public Faculty createUser(Faculty faculty) {
        return schoolService.create(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editStudent(Faculty faculty) {
        Faculty editFaculty = schoolService.update(faculty);
        if (editFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(faculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteStudent(@PathVariable("id") long id) {
        Faculty faculty = schoolService.remove(id);
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(faculty);
    }
}
