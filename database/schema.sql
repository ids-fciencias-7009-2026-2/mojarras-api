DROP TABLE IF EXISTS photos;
DROP TABLE IF EXISTS publications;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       zip_code VARCHAR(10) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP NULL
);

CREATE TABLE publications (
                              id BIGSERIAL PRIMARY KEY,
                              owner_id BIGINT NOT NULL REFERENCES users(id),
                              pet_name VARCHAR(100) NOT NULL,
                              description VARCHAR(1000) NOT NULL,
                              type VARCHAR(10) NOT NULL,
                              breed VARCHAR(100),
                              zip_code VARCHAR(10) NOT NULL,
                              status VARCHAR(10) NOT NULL DEFAULT 'DRAFT',
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE photos (
                        id BIGSERIAL PRIMARY KEY,
                        publication_id BIGINT NOT NULL REFERENCES publications(id) ON DELETE CASCADE,
                        url VARCHAR(500) NOT NULL
);