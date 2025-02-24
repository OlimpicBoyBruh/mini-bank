--liquibase formatted sql

--changeset Artem:2
--comment third migration
 create table account_service.account_type (
 account_id varchar(255) not null,
 account_name varchar(255),
 type varchar(255) check (type in ('DEPOSIT', 'ACCOUNT')),
 interest_rate float(53),
  replenishment_option boolean not null,
 withdrawal_option boolean not null,
 primary key (account_id));

 insert into account_type (account_id, account_name, type, interest_rate, replenishment_option, withdrawal_option)
 values ('12345', 'Сохраняй', 'DEPOSIT', 15.5, false, false);
 insert into account_type (account_id, account_name, type, interest_rate, replenishment_option, withdrawal_option)
 values ('23456', 'Управляй', 'DEPOSIT', 12.6, false, true);
 insert into account_type (account_id, account_name, type, interest_rate, replenishment_option, withdrawal_option)
 values ('34567', 'Пополняй', 'DEPOSIT', 10.3, true, false);
 insert into account_type (account_id, account_name, type, interest_rate, replenishment_option, withdrawal_option)
 values ('45678', 'Платежный счет', 'ACCOUNT', 0, true, true);
--rollback truncate table deposit_type;
