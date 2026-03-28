-- Proyecto: Plataforma de Adopción de Perros y Gatos
-- Equipo: Mojarras

-- Enlace al video de prueba de endpoits con postman:
    https://drive.google.com/file/d/1RfbiDdSGkZ-kThZ5rk7h03LlnaeLb29Z/view?usp=sharing


-- Base de datos: 

    Se usa PostgreSQL a través de Supabase.

    Requisitos previos:
        - Tener acceso a la instancia de Supabase del proyecto
        - Crear un archivo .env en la raíz del proyecto que por convención no se sube al repositorio. 
            ```
            URL_DB=<host>:<puerto>/<nombre_bd>?sslmode=require
            USER_DB=<usuario_de_supabase>
            PASSWORD_DB=<contraseña>
            ```

    Para construir la base de datos se debe conectar a una instancia de PostgreSQL con cualquier cliente SQL. Abrir el archivo `database/schema.sql` y ejecutar el archivo. 
    *El script elimina y recrea la tabla users.