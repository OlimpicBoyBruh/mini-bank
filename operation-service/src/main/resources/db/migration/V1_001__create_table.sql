create table operation
(
    id             bigserial primary key,
    amount         numeric(38, 2),
    credit_account varchar(10),
    debit_account  varchar(10),
    description    varchar(255),
    created_at     timestamp default current_timestamp,
    updated_at     timestamp default current_timestamp
);

insert into operation (amount, debit_account, credit_account, description)
values (100.00,'12345001','12345051','Зачисление наличных через банкомат'),
       (10.00,'12345051','12345052','Перевод на счет пользователя через приложение'),
       (50.00,'12345051','12345002','Оплата покупки в магазине'),
       (10.00,'12345052','12345001','Выдача наличных');
