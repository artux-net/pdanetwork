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


