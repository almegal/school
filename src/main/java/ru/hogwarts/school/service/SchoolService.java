package ru.hogwarts.school.service;

import java.util.Map;

public interface SchoolService<T> {
    public T create(T element);

    public T get(long id);

    public T remove(long id);

    public T update(T element);

    public Map<Long, T> byFilter(Object filter);
}
