package ru.hogwarts.school.service.interfaces;

import java.util.Collection;

public interface SchoolServiceFilterByColor<T> extends SchoolService<T> {
    public Collection<T> filterByColor(String color);
}
