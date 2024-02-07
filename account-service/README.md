## Структура проекта

### Сущности

#### deposits_clients
- Id_client
- number_deposit
- amount
- opening_date
- closing_date

#### accounts_clients
- id_client
- number_account
- amount
- opening_date

#### Deposit_type
- содержит информацию о доступных вкладах

### Контроллеры

#### Path:
- /deposits
- /accounts

#### DepositsController
- Отвечает за операции с вкладами

#### AccountsController
- Отвечает за операции со счетами

### User Story

1. Я хочу иметь возможность открывать/закрывать счета.
2. Я хочу иметь возможность открыть/закрыть вклады.
3. Я хочу получать информацию о своих счетах.
4. Я хочу видеть баланс.