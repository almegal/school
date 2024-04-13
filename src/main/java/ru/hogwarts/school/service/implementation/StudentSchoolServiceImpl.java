package ru.hogwarts.school.service.implementation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.SchoolService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Qualifier("StudentSchoolServiceImpl")
public class StudentSchoolServiceImpl implements SchoolService<Student> {
    private final Map<Long, Student> students = new HashMap<>();
    private long id = 0;

    @Override
    public Student create(Student student) {
        student.setId(++id);
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public Student get(long id) {
        return students.get(id);
    }

    @Override
    public Student remove(long id) {
        return students.remove(id);
    }

    @Override
    public Student update(Student student) {
        return students.computeIfPresent(student.getId(), (k, v) -> v = student);
    }

    @Override
    public Map<Long, Student> byFilter(Object age) {
        if (age.getClass() != Integer.class) {
            String error = String.format("метод byFilter ожидает int. Передан %s", age.getClass());
            throw new IllegalArgumentException(error);
        }
        int finalAge = (int) age;
        return students.values().stream()
                .filter(v -> v.getAge() == finalAge)
                .collect(Collectors.toMap(Student::getId, v -> v));
    }
}
