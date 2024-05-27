package ru.hogwarts.school.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultysRepository;
import ru.hogwarts.school.service.interfaces.SchoolServiceForFaculty;

import java.util.*;

@Service
public class FacultySchoolServiceImpl implements SchoolServiceForFaculty<Faculty> {
    private final FacultysRepository facultysRepository;
    Logger logger = LoggerFactory.getLogger(FacultySchoolServiceImpl.class);

    public FacultySchoolServiceImpl(FacultysRepository facultysRepository) {
        this.facultysRepository = facultysRepository;
    }

    @Override
    public Faculty create(Faculty faculty) {
        logger.info("Создание факультета {}, метод create", faculty);
        return facultysRepository.save(faculty);
    }

    @Override
    public Faculty get(long id) {
        logger.info("Получить факультет по ID: {}, метод get", id);
        return facultysRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Такого факультута нет в БД"));
    }

    @Override
    public void remove(long id) {
        logger.info("Удалить факультет по ID: {}, метод remove", id);
        boolean isExsist = facultysRepository.existsById(id);
        if (!isExsist) {
            logger.error("Ошибка в поиске факультета по ID: {}, метод remove", id);
            throw new EntityNotFoundException("Такого факультута нет в БД");
        }
        facultysRepository.deleteById(id);
    }

    @Override
    public Faculty update(Faculty faculty) {
        logger.info("Обновить факультет: {}, метод update", faculty);
        boolean isExsist = facultysRepository.existsById(faculty.getId());
        if (!isExsist) {
            logger.error("Ошибка в поиске факультета по ID: {}, метод update", faculty.getId());
            throw new EntityNotFoundException("Такого факультута нет в БД");
        }
        return facultysRepository.save(faculty);
    }

    @Override
    public Collection<Faculty> findAllByColorIgnoreCase(String color) {
        logger.info("Получить все факультеты по цвету: {}, метод findAllByColorIgnoreCase", color);
        return facultysRepository.findAllByColorIgnoreCase(color);
    }

    @Override
    public Faculty findByNameIgnoreCase(String name) {
        logger.info("Получить все факультеты по названию: {}, метод findByNameIgnoreCase", name);
        return facultysRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Set<Student> getStudentsInFaculty(Long id) {
        logger.info("Получить всех студентов факультета ID: {}, метод getStudentsInFaculty", id);
        Faculty faculty = get(id);
        return Collections.unmodifiableSet(faculty.getStudents());
    }

    public String getLongestNameFaculty() {
        List<Faculty> facultys = facultysRepository.findAll();
        return facultys.stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
    }
}
