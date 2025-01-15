CREATE TABLE IF NOT EXISTS status (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL
);
INSERT INTO status (name) VALUES
                              ('Active'),
                              ('NoActive');

CREATE TABLE managers (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);
