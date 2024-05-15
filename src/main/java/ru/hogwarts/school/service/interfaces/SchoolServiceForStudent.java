package ru.hogwarts.school.service.interfaces;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface SchoolServiceForStudent<T> extends SchoolService<T> {
    public Collection<T> filterByAge(int age);

    public Collection<T> findByAgeBetween(int min, int max);

    public Faculty getStudentsFaculty(Long id);

    public Integer getCountStudents();

    public Integer getaverageAgeStudents();

    public Collection<Student> getLastFiveStudents();
}
