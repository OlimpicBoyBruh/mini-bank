--liquibase formatted sql

 --changeset Artem:1
 --comment first migration
create table account_service.account_client (
id_client varchar(255),
number_account varchar(255) not null,
account_type_account_id varchar(255),
type varchar(255) check (type in ('DEPOSIT', 'ACCOUNT')),
balance float(53) not null,
 status varchar(255) check (status in ('ACTIVE','BLOCKED','CLOSED')),
opening_date timestamp(6),
closed_date timestamp(6),
 primary key (number_account));

insert into account_client (id_client, number_account, account_type_account_id, type,
 balance, status, opening_date, closed_date)
values ('BANK-ACCOUNT', '400000000000000000000', 45678 , 'ACCOUNT', 0, 'ACTIVE', CURRENT_TIMESTAMP, NULL);
--rollback truncate table accounts_clients;

