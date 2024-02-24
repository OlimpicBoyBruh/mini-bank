## Полезная информация

* [Доска для ведения задач](https://ru.yougile.com/team/f64e00f9f39b/%D0%9C%D0%B8%D0%BD%D0%B8%D0%91%D0%B0%D0%BD%D0%BA])
* [GitHub репозитарий](https://github.com/DanilaTatara/MiniBank-JavaDev-1023)
* [Инструмент для создания диаграмм](https://www.planttext.com/) ([Инструкция](https://plantuml.com/ru/guide))

## Правила работы с git

1. Feature-ветки создаются от `develop`
2. Вконце работы над фичей для уменьшения merge-конфликтов выполняется `git rebase develop`.
   Перед этим ветка `develop` обязательно обновляется:

```
git checkout develop
git pull
```

3. Создается pull-реквест из feature-ветки в `develop`
4. После утверждения pull-реквеста выполняется слияние feature-ветки в `develop`

## Описание сервиса счетов
[Документация по сервису](../account-service/account-service.md)
