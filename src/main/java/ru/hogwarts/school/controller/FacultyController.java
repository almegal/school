package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.implementation.FacultySchoolServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping("facultys")
public class FacultyController {
    private final FacultySchoolServiceImpl schoolService;

    public FacultyController(FacultySchoolServiceImpl schoolService) {
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
    public ResponseEntity<Collection<Faculty>> filterByColor(@PathVariable("color") String color) {
        Collection<Faculty> result = schoolService.findAllByColorIgnoreCase(color);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Faculty> getByName(@PathVariable("name") String name) {
        Faculty result = schoolService.findByNameIgnoreCase(name);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

    @PostMapping
    public Faculty createFaculty(Faculty faculty) {
        return schoolService.create(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(Faculty faculty) {
        Faculty result;
        try {
            result = schoolService.update(faculty);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable("id") long id) {
        try {
            schoolService.remove(id);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
