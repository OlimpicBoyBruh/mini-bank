## Структура проекта

### Сущности

#### account_type

- depositId;
- accountName;
- interestRate;
- replenishmentOption;
- withdrawalOption;
- type;
- depositsClients;

#### account_client

- numberAccount;
- balance;
- openingDate;
- closedDate;
- status;
- accountType;

### Контроллеры

#### ProfileController

- Отвечает за отображение информации у авторизированного пользователя

### User Story

1. Я хочу иметь возможность открывать/закрывать счета.
2. Я хочу иметь возможность открыть/закрыть вклады.
3. Я хочу получать информацию о своих счетах.
4. Я хочу видеть баланс.
