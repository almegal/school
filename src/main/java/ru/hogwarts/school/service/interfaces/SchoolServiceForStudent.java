package ru.hogwarts.school.service.interfaces;

import java.util.Collection;

public interface SchoolServiceForStudent<T> extends SchoolService<T> {
    public Collection<T> filterByAge(int age);

    public Collection<T> findByAgeBetween(int min, int max);
}
