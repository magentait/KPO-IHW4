-- liquibase formatted sql
-- changeset drbezrukov@edu.hse.ru:1 logicalFilePath:1.0.0/users.sql
DROP TABLE IF EXISTS users;
create table users
(
    id uuid default gen_random_uuid()
        primary key,
    name varchar(50)         not null,
    email    varchar(100) unique not null,
    password varchar(255)        not null,
    created  timestamp
)

-- rollback delete users;
