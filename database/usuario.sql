CREATE TABLE Usuario ( id SERIAL PRIMARY KEY,
                       nombre VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(20) NOT NULL,
                       token VARCHAR(50) NOT NULL);