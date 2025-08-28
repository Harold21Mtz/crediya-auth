CREATE TABLE role
(
    role_id     BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE "user"
(
    user_id         BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    lastname        VARCHAR(100) NOT NULL,
    birth_date      DATE         NOT NULL,
    document_number VARCHAR(50)  NOT NULL UNIQUE,
    phone           VARCHAR(20),
    email           VARCHAR(150) UNIQUE,
    address         VARCHAR(255),
    base_salary     NUMERIC(15, 2),
    role_id         BIGINT       NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role (role_id)
);

INSERT INTO role (name, description)
VALUES
    ('ADMIN', 'Administrador con acceso total al sistema'),
    ('USER', 'Usuario estándar con permisos limitados');

