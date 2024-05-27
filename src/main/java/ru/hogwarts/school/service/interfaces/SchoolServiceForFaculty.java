package ru.hogwarts.school.service.interfaces;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Set;

public interface SchoolServiceForFaculty<T> extends SchoolService<T> {
    public Collection<T> findAllByColorIgnoreCase(String color);

    public T findByNameIgnoreCase(String name);

    Set<Student> getStudentsInFaculty(Long id);
}
