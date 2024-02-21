create table operation
(
    id             bigserial primary key,
    amount         numeric(38, 2),
    credit_account varchar(20),
    debit_account  varchar(20),
    description    varchar(255),
    created_at     timestamp default current_timestamp,
    updated_at     timestamp default current_timestamp
);

insert into operation (amount, debit_account, credit_account, description)
values (100.00, '40802810123450000001', '40802810123412345051', 'Зачисление наличных через банкомат'),
       (10.00, '40802810123412345051', '40802810123412345052', 'Перевод на счет пользователя через приложение'),
       (50.00, '40802810123412345051', '40802810123412345002', 'Оплата покупки в магазине'),
       (10.00, '40802810123412345052', '40802810123412345001', 'Выдача наличных');
