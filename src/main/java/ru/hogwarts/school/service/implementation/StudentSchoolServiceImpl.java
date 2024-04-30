package ru.hogwarts.school.service.implementation;

import org.springframework.stereotype.Service;
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
        return studentsRepository.findById(id).get();
    }

    @Override
    public void remove(long id) {
        boolean isExsist = studentsRepository.existsById(id);
        if (!isExsist) {
            throw new RuntimeException("Такого студента нет в БД");
        }
        studentsRepository.deleteById(id);
    }

    @Override
    public Student update(Student student) {
        boolean isExsist = studentsRepository.existsById(student.getId());
        if (!isExsist) {
            throw new RuntimeException("Такого студента нет в БД");
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
}
