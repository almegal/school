package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

@Repository
public interface StudentsRepository extends JpaRepository<Student, Long> {
    public Collection<Student> findAllByAge(int age);
}
