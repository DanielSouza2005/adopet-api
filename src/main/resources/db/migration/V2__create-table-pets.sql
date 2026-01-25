create table pets
(
    id        bigint generated always as identity,
    tipo      varchar(100)  not null,
    nome      varchar(100)  not null,
    raca      varchar(100)  not null,
    idade     integer       not null,
    cor       varchar(100)  not null,
    peso      numeric(4, 2) not null,
    abrigo_id bigint        not null,
    adotado   boolean       not null,
    primary key (id),
    constraint fk_pets_abrigo_id
        foreign key (abrigo_id)
            references abrigos (id)
);