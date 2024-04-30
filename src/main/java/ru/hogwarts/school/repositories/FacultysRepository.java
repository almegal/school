package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

@Repository
public interface FacultysRepository extends JpaRepository<Faculty, Long> {
    public Collection<Faculty> findAllByColorIgnoreCase(String color);

    public Faculty findByNameIgnoreCase(String name);
}
