create schema if not exists management;

create table management.t_user
(
    id       serial primary key,
    username varchar not null check ( length(trim(username)) > 0 ) unique,
    password varchar
);

create table management.t_authority
(
    id        serial primary key,
    authority varchar not null check ( length(trim(authority)) > 0 ) unique
);

create table management.t_user_authority
(
    id           serial primary key,
    id_user      int not null references management.t_user (id),
    id_authority int not null references management.t_authority (id),
    constraint uc_user_authority unique (id_user, id_authority)
);
