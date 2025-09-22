# Stalker PDA Network Backend
![Deploy dev](https://github.com/artux-net/pdanetwork/actions/workflows/master.yml/badge.svg)
![DB Backup](https://github.com/artux-net/pdanetwork/actions/workflows/backup.yml/badge.svg)
![Test](https://github.com/artux-net/pdanetwork/actions/workflows/test.yml/badge.svg)

Монолитный бэкенд пда, отвечает за авторизацию, новости, чаты, энциклопедию, рейтинг, сюжетные истории и обрабатывает сюжетные действия

## Services

### dev среда
<a href="https://dev.artux.net/pdanetwork/swagger-ui/index.html">
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white" />
</a>
<a href="https://grafana.artux.net/d/twqdYjziz/micrometer-spring-throughput?orgId=1&var-application=&var-instance=dev.artux.net:80">
    <img src="https://img.shields.io/badge/Grafana-F2F4F9?style=for-the-badge&logo=grafana&logoColor=orange&labelColor=F2F4F9" />
</a>

 - [панель управления пользователями](https://dev.artux.net/panel) 

### prod среда

<a href="https://app.artux.net/pdanetwork/swagger-ui/index.html">
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white" />
</a>
<a href="https://grafana.artux.net/d/twqdYjziz/micrometer-spring-throughput?orgId=1&var-application=&var-instance=app.artux.net:80">
    <img src="https://img.shields.io/badge/Grafana-F2F4F9?style=for-the-badge&logo=grafana&logoColor=orange&labelColor=F2F4F9" />
</a>

- для доступа к Swagger UI необходима регистрация и роль пользователя начиная от TESTER включительно, от Grafana данные те же
- панель управления пользователями по адресу https://app.artux.net/panel

## Локальный запуск
Для локального пуска запустить базу данных через pdanet/docker-compose.yml

Затем уже точка входа `PDANetworkApplication.main()`

# Работа с квестами
Осуществляется через консоль https://story.artux.net/, использовать пароль от DEV среды

# Семантическое версионирование

Проект использует автоматическое семантическое версионирование. После каждого успешного деплоя создается новый тег в формате `v1.2.3`.

## Управление версиями

### Автоматическое версионирование по коммитам:

- **PATCH** (v1.0.1): Обычные коммиты, багфиксы
- **MINOR** (v1.1.0): Коммиты с `feat:`, `feature:`, `[minor]`  
- **MAJOR** (v2.0.0): Коммиты с `BREAKING CHANGE`, `!:`, `[major]`

### Примеры:

```bash
git commit -m "fix: исправлена ошибка логина"          # → PATCH
git commit -m "feat: добавлена новая функция"         # → MINOR  
git commit -m "feat!: критические изменения в API"    # → MAJOR
```

Теги создаются после деплоя в production или при push в main ветку.


