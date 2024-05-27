package ru.hogwarts.school.controller;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigForControllerTest {
    public final static String HOST = "http://localhost:";
    public final static String FACULTY_NAME = "Bluemond";
    public final static String FACULTY_COLOR = "Blue";
    public final static Faculty FACULTY_DEFAULT = new Faculty(FACULTY_NAME, FACULTY_COLOR);
    public final static List<Faculty> FACULTIES_COLLECTION = new ArrayList<>(Arrays.asList(
            new Faculty("Redmond", "Red"),
            new Faculty("Greenmond", "Green"),
            new Faculty("Blackmond", "Black"),
            new Faculty("Somemond", "Red")
    ));
    public final static List<Student> STUDENT_COLLECTION = new ArrayList<>(Arrays.asList(
            new Student(20, "Will Smith"),
            new Student(35, "Ruki Bazuki"),
            new Student(20, "Harry Potter"),
            new Student(50, "James Bond")
    ));
    public final static String STUDENT_NAME = "Russell Crowe";
    public final static int STUDENT_AGE = 65;
    public final static Student STUDENT_DEFAULT = new Student(STUDENT_AGE, STUDENT_NAME);

    private ConfigForControllerTest() {
    }
}
