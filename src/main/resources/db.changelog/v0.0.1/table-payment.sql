--liquibase formatted sql
--changeset tregubchenko:initial

create table if not exists payment (
    id                  UUID,
    order_name          varchar,
    price               integer,
    status              varchar,
    date                date,
    version             bigint      default 0,
    constraint pk_payment primary key (id)
);

--rollback drop table if not exists payment cascade;