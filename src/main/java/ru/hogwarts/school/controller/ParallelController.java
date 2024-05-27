package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.GetIntegerServiceImpl;
import ru.hogwarts.school.service.implementation.FacultySchoolServiceImpl;
import ru.hogwarts.school.service.implementation.StudentSchoolServiceImpl;

import java.util.List;

@RestController
public class ParallelController {
    final private StudentSchoolServiceImpl studentSchoolService;
    final private FacultySchoolServiceImpl facultySchoolService;
    final private GetIntegerServiceImpl getIntegerService;

    public ParallelController(StudentSchoolServiceImpl studentSchoolService,
                              FacultySchoolServiceImpl facultySchoolService,
                              GetIntegerServiceImpl getIntegerService) {
        this.studentSchoolService = studentSchoolService;
        this.facultySchoolService = facultySchoolService;
        this.getIntegerService = getIntegerService;
    }

    @GetMapping("students/get/names/start/a")
    @Operation(summary = "Получения всех имен всех студентов, чье имя начинается с буквы А.")
    public ResponseEntity<List<String>> getAllSortedStudent() {
        List<String> resutl = studentSchoolService.findAllStudentNameStartWithAUppercaseSorted();
        if (resutl == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resutl);
    }

    @GetMapping("students/average")
    @Operation(summary = "Получить средний возраст всех студентов")
    public ResponseEntity<Integer> getAverageOfStudents() {
        Integer avg = studentSchoolService.getAverageAgeByStream();
        return ResponseEntity.ok(avg);
    }

    @GetMapping("faculty/longest/name")
    @Operation(summary = "Получить самое длинное название факультета.")
    public ResponseEntity<String> getLongestFacultyName() {
        String result = facultySchoolService.getLongestNameFaculty();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get/itenger/parallel")
    @Operation(summary = "Получить целочисленное значение парарлельное вычисление")
    public ResponseEntity<Integer> getIntegerParallel() {
        Integer result = getIntegerService.getIntegerParallel();
        return ResponseEntity.ok(result);
    }
}
