package ru.hogwarts.school.service.interfaces;

public interface SchoolService<T> {
    public T create(T element);

    public T get(long id);

    public void remove(long id);

    public T update(T element);
}
