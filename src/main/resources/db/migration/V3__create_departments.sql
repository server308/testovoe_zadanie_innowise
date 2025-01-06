CREATE TABLE IF NOT EXISTS departments (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL
);

INSERT INTO departments (name) VALUES
                                   ('Human Resources'),
                                   ('Finance'),
                                   ('IT Support'),
                                   ('Marketing');

CREATE TABLE IF NOT EXISTS employee_department (
                                     employee_id BIGINT NOT NULL,
                                     department_id BIGINT NOT NULL,
                                     PRIMARY KEY (employee_id, department_id),
                                     FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
                                     FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);