package ru.hogwarts.school.service.implementation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.SchoolService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Qualifier("FacultySchoolServiceImpl")
public class FacultySchoolServiceImpl implements SchoolService<Faculty> {
    private final Map<Long, Faculty> facultys = new HashMap<>();
    private long id = 0;

    @Override
    public Faculty create(Faculty faculty) {
        faculty.setId(++id);
        facultys.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty get(long id) {
        return facultys.get(id);
    }

    @Override
    public Faculty remove(long id) {
        return facultys.remove(id);
    }

    @Override
    public Faculty update(Faculty faculty) {
        return facultys.computeIfPresent(faculty.getId(), (k, v) -> v = faculty);
    }

    @Override
    public Map<Long, Faculty> byFilter(Object color) {
        if (color.getClass() != String.class) {
            String error = String.format("метод byFilter ожидает строку. Передан %s", color.getClass());
            throw new IllegalArgumentException(error);
        }
        return facultys.values().stream()
                .filter(v -> v.getColor().equals(color))
                .collect(Collectors.toMap(Faculty::getId, v -> v));
    }
}
