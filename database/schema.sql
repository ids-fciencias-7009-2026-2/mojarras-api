DROP TABLE IF EXISTS interests;
DROP TABLE IF EXISTS photos;
DROP TABLE IF EXISTS verification_tokens;
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
                       is_verified BOOLEAN NOT NULL DEFAULT false,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP NULL
);

CREATE TABLE verification_tokens (
                        id BIGSERIAL PRIMARY KEY,
                        token VARCHAR(255) NOT NULL UNIQUE,
                        user_id BIGINT NOT NULL,
                        expiry_date TIMESTAMP NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_verification_token_user
                             FOREIGN KEY (user_id)
                                 REFERENCES users(id)
                                 ON DELETE CASCADE
);

CREATE TABLE publications (
                              id BIGSERIAL PRIMARY KEY,

                              owner_id BIGINT NOT NULL,
                              CONSTRAINT fk_publication_owner
                                  FOREIGN KEY (owner_id) REFERENCES users(id)
                                      ON DELETE CASCADE,

                              pet_name VARCHAR(100) NOT NULL,
                              description VARCHAR(1000) NOT NULL,

                              type VARCHAR(20) NOT NULL,
                              breed VARCHAR(100),

                              zip_code VARCHAR(10) NOT NULL,

                              status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',

                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE photos (
                        id BIGSERIAL PRIMARY KEY,

                        publication_id BIGINT NOT NULL,
                        CONSTRAINT fk_photo_publication
                            FOREIGN KEY (publication_id)
                                REFERENCES publications(id)
                                ON DELETE CASCADE,

                        url VARCHAR(500) NOT NULL
);

CREATE TABLE interests (
                           id BIGSERIAL PRIMARY KEY,

                           publication_id BIGINT NOT NULL,
                           interested_user_id BIGINT NOT NULL,

                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_interest_publication
                               FOREIGN KEY (publication_id)
                                   REFERENCES publications(id)
                                   ON DELETE CASCADE,

                           CONSTRAINT fk_interest_user
                               FOREIGN KEY (interested_user_id)
                                   REFERENCES users(id)
                                   ON DELETE CASCADE,

                           CONSTRAINT uq_interest UNIQUE (publication_id, interested_user_id)
);
