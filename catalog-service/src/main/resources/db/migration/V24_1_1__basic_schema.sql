create schema if not exists catalog;

create table catalog.t_product
(
    id      serial primary key,
    title   varchar(50) not null check (length(trim(title)) >= 3 ),
    details varchar(1000)
);