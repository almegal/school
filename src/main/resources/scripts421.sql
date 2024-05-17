ALTER TABLE student
    ALTER COLUMN name SET NOT NULL,
    ALTER COLUMN age SET DEFAULT 20,
    ADD CONSTRAINT age_check CHECK (age >= 16),
    ADD CONSTRAINT student_name_check unique (name);

ALTER TABLE faculty
    ADD CONSTRAINT color_check unique (color),
    ADD CONSTRAINT faculty_name_check unique (name);
