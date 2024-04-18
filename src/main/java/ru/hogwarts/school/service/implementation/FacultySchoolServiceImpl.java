package ru.hogwarts.school.service.implementation;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultysRepository;
import ru.hogwarts.school.service.interfaces.SchoolServiceFilterByColor;

import java.util.Collection;

@Service
public class FacultySchoolServiceImpl implements SchoolServiceFilterByColor<Faculty> {
    private final FacultysRepository facultysRepository;

    public FacultySchoolServiceImpl(FacultysRepository facultysRepository) {
        this.facultysRepository = facultysRepository;
    }

    @Override
    public Faculty create(Faculty faculty) {
        return facultysRepository.save(faculty);
    }

    @Override
    public Faculty get(long id) {
        return facultysRepository.findById(id).orElseThrow();
    }

    @Override
    public void remove(long id) {
        boolean isExsist = facultysRepository.existsById(id);
        if (!isExsist) {
            throw new RuntimeException("Такого факультута нет в БД");
        }
        facultysRepository.deleteById(id);
    }

    @Override
    public Faculty update(Faculty faculty) {
        boolean isExsist = facultysRepository.existsById(faculty.getId());
        if (!isExsist) {
            throw new RuntimeException("Такого факультута нет в БД");
        }
        return facultysRepository.save(faculty);
    }

    @Override
    public Collection<Faculty> filterByColor(String color) {
        return facultysRepository.findAllByColor(color);
    }
}
