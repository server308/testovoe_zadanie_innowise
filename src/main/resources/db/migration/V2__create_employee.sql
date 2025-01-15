CREATE TABLE IF NOT EXISTS employees (
                                         id SERIAL PRIMARY KEY,
                                         first_name VARCHAR(255) NOT NULL,
                                         last_name VARCHAR(255) NOT NULL,
                                         email VARCHAR(255) NOT NULL UNIQUE,
                                         phone_number VARCHAR(15),
                                         status_id BIGINT,
                                         manager_id BIGINT,
                                         deleted BOOLEAN DEFAULT FALSE,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                         FOREIGN KEY (status_id) REFERENCES status(id) ON DELETE SET NULL,
                                         FOREIGN KEY (manager_id) REFERENCES managers(id) ON DELETE SET NULL
);
