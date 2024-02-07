--liquibase formatted sql

--changeset Artem:3
--comment third migration
create table deposit_type (
deposit_id varchar(25) not null,
deposit_name varchar(25) not null,
interest_rate float(53) not null,
 max_term integer not null,
  replenishment_option boolean not null,
   withdrawal_option boolean not null,
     primary key (deposit_id));

     insert into deposit_type(deposit_id, deposit_name,
     interest_rate, max_term,
      replenishment_option,
       withdrawal_option) values ('1234567890','Test', 10, 36, true, true);
--rollback truncate table deposit_type;
