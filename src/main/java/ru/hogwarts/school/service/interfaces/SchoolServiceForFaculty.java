package ru.hogwarts.school.service.interfaces;

import java.util.Collection;

public interface SchoolServiceForFaculty<T> extends SchoolService<T> {
    public Collection<T> findAllByColorIgnoreCase(String color);

    public T findByNameIgnoreCase(String name);
}
