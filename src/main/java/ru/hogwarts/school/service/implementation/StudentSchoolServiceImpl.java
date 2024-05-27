package ru.hogwarts.school.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentsRepository;
import ru.hogwarts.school.service.interfaces.SchoolServiceForStudent;

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StudentSchoolServiceImpl implements SchoolServiceForStudent<Student> {
    private final StudentsRepository studentsRepository;
    Logger logger = LoggerFactory.getLogger(StudentSchoolServiceImpl.class);

    public StudentSchoolServiceImpl(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    @Override
    public Student create(Student student) {
        logger.info("Создание студента {}, метод create", student);
        return studentsRepository.save(student);
    }

    @Override
    public Student get(long id) {
        logger.info("Получение студента по ID: {}, метод get", id);
        return studentsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ошибка в поиске студента. ID: {}, метод get", id);
                    return new EntityNotFoundException("Такого студента нет в БД");
                });
    }

    @Override
    public void remove(long id) {
        logger.info("Удаление студента по ID: {}, метод remove", id);
        boolean isExsist = studentsRepository.existsById(id);
        if (!isExsist) {
            logger.error("Ошибка в поиске студента. ID: {}, метод remove", id);
            throw new EntityNotFoundException("Такого студента нет в БД");
        }
        studentsRepository.deleteById(id);
    }

    @Override
    public Student update(Student student) {
        logger.info("Обновление студента {}, метод update", student);
        boolean isExsist = studentsRepository.existsById(student.getId());
        if (!isExsist) {
            logger.error("Ошибка в поиске студента. ID: {}, метод update", student.getId());
            throw new EntityNotFoundException("Такого студента нет в БД");
        }
        return studentsRepository.save(student);
    }

    @Override
    public Collection<Student> filterByAge(int age) {
        logger.info("Получить студентов по возрасту {}, метод filterByAge", age);
        return studentsRepository.findAllByAge(age);
    }

    @Override
    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Получить студентов возрастом от {} до {}, метод findByAgeBetween", min, max);
        return studentsRepository.findByAgeBetween(min, max);
    }

    @Override
    public Faculty getStudentsFaculty(Long id) {
        logger.info("Получить факультет студента. Студент ID: {}, метод getStudentsFaculty", id);
        final Student student = get(id);
        return student.getFaculty();
    }

    @Override
    public Integer getCountStudents() {
        logger.info("Получить количество студентов, метод getCountStudents");
        return studentsRepository.getCountStudents();
    }

    @Override
    public Integer getaverageAgeStudents() {
        logger.info("Получить средний возраст студентов, метод getaverageAgeStudents");
        return studentsRepository.getAverageAgeStudents();
    }

    @Override
    public Collection<Student> getLastFiveStudents() {
        logger.info("Получить последних 5 студентов из БД, метод getLastFiveStudents");
        return studentsRepository.getLastFiveStudents();
    }

    public List<String> findAllStudentNameStartWithAUppercaseSorted() {
        List<Student> students = studentsRepository.findAll();
        return students.stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith("A"))
                .sorted()
                .toList();
    }

    public Integer getAverageAgeByStream() {
        List<Student> students = studentsRepository.findAll();
        Optional<Integer> sum = students.stream()
                .map(Student:: getAge)
                .reduce(Integer::sum);
        return sum.orElseThrow() / students.size();
    }
}
