--liquibase formatted sql

--changeset Artem:2
--comment second migration
create table deposits_clients (
number_account varchar(255) not null,
id_client varchar(255),
amount float(53) not null,
account_status varchar(20),
 closing_date timestamp(6),
 opening_date timestamp(6),
 deposit_type_deposit_id varchar(255),
 primary key (number_account));
--rollback truncate table deposits_clients;

