--liquibase formatted sql

 --changeset Artem:1
 --comment first migration
create table account_client (
number_account varchar(255) not null,
id_client varchar(255) not null,
balance float(53) not null,
 opening_date timestamp(6) not null,
 closed_date timestamp(6) not null,
  account_type_deposit_id varchar(255) not null,
  status varchar(255),
    primary key (number_account));


--rollback truncate table accounts_clients;

