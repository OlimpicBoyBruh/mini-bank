--liquibase formatted sql

 --changeset Artem:1
 --comment first migration
create table account_service.account_client (
id_client varchar(255),
number_account varchar(255) not null,
account_type_account_id varchar(255),
type varchar(255),
balance float(53) not null,
 status varchar(255) check (status in ('ACTIVE','BLOCKED','CLOSED')),
opening_date timestamp(6),
closed_date timestamp(6),
 primary key (number_account));


--rollback truncate table accounts_clients;

