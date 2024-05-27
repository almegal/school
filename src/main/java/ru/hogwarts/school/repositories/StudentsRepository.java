package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

@Repository
public interface StudentsRepository extends JpaRepository<Student, Long> {
    public Collection<Student> findAllByAge(int age);

    public Collection<Student> findByAgeBetween(int min, int max);

    @Query(value = "select count(*) from student", nativeQuery = true)
    public Integer getCountStudents();

    @Query(value = "select avg(age) from student", nativeQuery = true)
    public Integer getAverageAgeStudents();

    @Query(value = "select * from student order by id desc limit 5", nativeQuery = true)
    public Collection<Student> getLastFiveStudents();
}
