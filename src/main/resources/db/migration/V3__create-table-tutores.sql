create table tutores
(
    id       bigint generated always as identity,
    nome     varchar(100) not null,
    telefone varchar(14)  not null unique,
    email    varchar(100) not null unique,
    primary key (id)
);