package ru.hogwarts.school.service.interfaces;

import java.util.Collection;

public interface SchoolServiceFilterByAge<T> extends SchoolService<T> {
    public Collection<T> filterByAge(int age);

}
