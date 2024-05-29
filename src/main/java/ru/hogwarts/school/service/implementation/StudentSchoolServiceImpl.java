package ru.hogwarts.school.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentsRepository;
import ru.hogwarts.school.service.interfaces.SchoolServiceForStudent;

import java.util.Collection;
import java.util.List;

@Service
public class StudentSchoolServiceImpl implements SchoolServiceForStudent<Student> {
    private final StudentsRepository studentsRepository;
    int count = 0;
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

    private synchronized void printSyncStudent(Student student) {
        synchronized (new Object()) {
            System.out.println(count);
            System.out.println(Thread.currentThread().getName() + " " + student);
            count++;
        }
    }

    public void printSynchronized() {
        List<Student> students = studentsRepository.findAll();

        printSyncStudent(students.get(0));
        printSyncStudent(students.get(1));

        Runnable task1 = () -> {
            printSyncStudent(students.get(2));
            printSyncStudent(students.get(3));
        };
        Runnable task2 = () -> {
            printSyncStudent(students.get(4));
            printSyncStudent(students.get(5));
        };

        new Thread(task1).start();
        new Thread(task2).start();
    }

    public void printParallel() {
        List<Student> students = studentsRepository.findAll();

        System.out.println(Thread.currentThread().getName() + " " + students.get(0));
        System.out.println(Thread.currentThread().getName() + " " + students.get(1));

        Runnable task1 = () -> {
            System.out.println(Thread.currentThread().getName() + " " + students.get(2));
            System.out.println(Thread.currentThread().getName() + " " + students.get(3));
        };
        Runnable task2 = () -> {
            System.out.println(Thread.currentThread().getName() + " " + students.get(4));
            System.out.println(Thread.currentThread().getName() + " " + students.get(5));
        };

        new Thread(task1).start();
        new Thread(task2).start();

    }
}
