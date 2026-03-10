# Changelog

Все существенные изменения в этом проекте будут записаны в этом файле.

## [1.0.0] - 2026-03-10

### Рутина

- Релиз
- *(repo)* Добавлен файл .gitattributes с правилами export-ignore
- *(repo)* Добавлен файл CHANGELOG.md в .gitattributes с правилом export-ignore

### CI/CD

- *(changelog)* Добавлена конфигурация для генерации changelog на русском языке
- *(changelog)* Исправлены header и footer, а также добавлена новая категория commit-ов в конфигурации changelog
- *(changelog)* Добавлена настройка filter_unconventional в конфигурации changelog
- *(workflow)* Изменен тип триггера для релизов с created на published в скрипте для обновления changelog

<!-- сгенерировано с помощью git-cliff -->
