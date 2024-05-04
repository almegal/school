package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.implementation.FacultySchoolServiceImpl;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("facultys")
public class FacultyController {
    private final FacultySchoolServiceImpl schoolService;

    public FacultyController(FacultySchoolServiceImpl schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить факультет по id")
    public ResponseEntity<Faculty> get(@PathVariable("id") long id) {
        Faculty faculty = schoolService.get(id);
        return ResponseEntity.ok(faculty);

    }

    @GetMapping("/{id}/students")
    @Operation(summary = "Получить всех студентов факультета")
    public ResponseEntity<Set<Student>> getStudentsInFaculty(@PathVariable("id") Long id) {
        Set<Student> result = schoolService.getStudentsInFaculty(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

    @GetMapping("/color/{color}")
    @Operation(summary = "Получить список факультетов по цвету")
    public ResponseEntity<Collection<Faculty>> filterByColor(@PathVariable("color") String color) {
        Collection<Faculty> result = schoolService.findAllByColorIgnoreCase(color);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Получить факультет по названию")
    public ResponseEntity<Faculty> getByName(@PathVariable("name") String name) {
        Faculty result = schoolService.findByNameIgnoreCase(name);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

    @PostMapping
    @Operation(summary = "Создать факультет")
    public Faculty createFaculty(Faculty faculty) {
        return schoolService.create(faculty);
    }

    @PutMapping
    @Operation(summary = "Обновить факультет")
    public ResponseEntity<Faculty> editFaculty(Faculty faculty) {
        Faculty result = schoolService.update(faculty);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить факультет")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable("id") long id) {
        schoolService.remove(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
