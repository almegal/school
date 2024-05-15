package ru.hogwarts.school.service.implementation;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentsRepository;
import ru.hogwarts.school.service.interfaces.SchoolServiceForStudent;

import java.util.Collection;

@Service
public class StudentSchoolServiceImpl implements SchoolServiceForStudent<Student> {
    private final StudentsRepository studentsRepository;

    public StudentSchoolServiceImpl(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    @Override
    public Student create(Student student) {
        return studentsRepository.save(student);
    }

    @Override
    public Student get(long id) {
        return studentsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Такого студента нет в БД"));
    }

    @Override
    public void remove(long id) {
        boolean isExsist = studentsRepository.existsById(id);
        if (!isExsist) {
            throw new EntityNotFoundException("Такого студента нет в БД");
        }
        studentsRepository.deleteById(id);
    }

    @Override
    public Student update(Student student) {
        boolean isExsist = studentsRepository.existsById(student.getId());
        if (!isExsist) {
            throw new EntityNotFoundException("Такого студента нет в БД");
        }
        return studentsRepository.save(student);
    }

    @Override
    public Collection<Student> filterByAge(int age) {
        return studentsRepository.findAllByAge(age);
    }

    @Override
    public Collection<Student> findByAgeBetween(int min, int max) {
        return studentsRepository.findByAgeBetween(min, max);
    }

    @Override
    public Faculty getStudentsFaculty(Long id) {
        final Student student = get(id);
        return student.getFaculty();
    }

    @Override
    public Integer getCountStudents() {
        return studentsRepository.getCountStudents();
    }

    @Override
    public Integer getaverageAgeStudents() {
        return studentsRepository.getAverageAgeStudents();
    }

    @Override
    public Collection<Student> getLastFiveStudents() {
        return studentsRepository.getLastFiveStudents();
    }
}
