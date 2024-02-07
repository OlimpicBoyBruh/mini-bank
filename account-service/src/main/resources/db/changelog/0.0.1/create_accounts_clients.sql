--liquibase formatted sql

 --changeset Artem:1
 --comment first migration
 create table accounts_clients (
 number_account varchar(25) not null,
 id_client varchar(255),
 amount float(53) not null,
 account_status varchar(20),
 opening_date timestamp(6),
 primary key (number_account));

--rollback truncate table accounts_clients;

