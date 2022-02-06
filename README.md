**FROM openjdk:16-alpine3.13**
**FROM arm64v8/openjdk:16**

# PDA Network
##Setup server
На сервере необходим развернутый докер с доступом по сети к его демону.
После первого запуска compose необходимо задать настройки nginx в папке -

    sudo nano /apps/config/nginx/site-confs/default

Файл должен включать следующее содержимое:

        upstream backend {
            server app:8080;
        }
        
        upstream express{
            server mongo-express:8081;
        }
        
        server{
            server_name app.artux.net 143.47.186.141;        
            proxy_connect_timeout 600;
            proxy_send_timeout 600;
            proxy_read_timeout 600;
            send_timeout 600;
            proxy_buffering off;
            ignore_invalid_headers off;
            
            location / {
                    add_header X-Frame-Options SAMEORIGIN always;
                    proxy_set_header X-Forwarded-Host $host;
                    proxy_set_header X-Forwarded-Server $host;
                    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                    proxy_pass http://backend;
            }
    
            location /mongo/utility {
                    proxy_set_header X-Forwarded-Host $host;
                    proxy_set_header X-Forwarded-Server $host;
                    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                    proxy_pass http://express/;
            }
    
              location /pdanetwork/dialog{
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection "upgrade";
                proxy_set_header HOST $host;
                proxy_set_header X_Forwarded_For $remote_addr;
                proxy_pass http://backend;
                proxy_redirect default;
                client_max_body_size 1000m;
            }
            location /pdanetwork/dialogs{
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection "upgrade";
                proxy_set_header HOST $host;
                proxy_set_header X_Forwarded_For $remote_addr;
                proxy_pass http://backend;
                proxy_redirect default;
                client_max_body_size 1000m;
            }
             location /pdanetwork/chat{
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection "upgrade";
                proxy_set_header HOST $host;
                proxy_set_header X_Forwarded_For $remote_addr;
                proxy_pass http://backend;
                proxy_redirect default;
                client_max_body_size 1000m;
            }
            location /pdanetwork/groups{
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection "upgrade";
                proxy_set_header HOST $host;
                proxy_set_header X_Forwarded_For $remote_addr;
                proxy_pass http://backend;
                proxy_redirect default;
                client_max_body_size 1000m;
            }
        }

PDA Network with API for application "Сталкерский ПДА"

### Groups

`0` - Одиночки

`1` - Бандиты

`2` - Военные

`3` - Свобода

`4` - Долг

`5` - Монолит

`6` - Наёмники

###Rangs
0-500 = отмычка 

500-1500 = новичок

1500-3500 = сталкер

3500-6000 = опытный

6000-9000 = ветеран

9000-12000 = мастер

12000-22000 = легенда


# Документация по работе квестов
## Введение
Важная часть пда это квесты, квестовый движок был разработан еще в 2019 году и не сильно с того времени модифицировался.
Поэтому, надо понимать что любые предложения по улучшению структуры файлов и работы движка только приветствуются.

## Основные понятия
### История (Story)
Представляет собой набор глав, который может отображаться в начальном меню ПДА.
Полноценной загрузки истории на устройство не происходит, загрузка истории начинается с 1 главы, 0 стадии и далее история следует в необходимом порядке.

### Глава (Chapter)
Глава представляет собой набор стадий и ссылок на музыку

### Стадия, экран, сцена
Стадия самое малое звено всей цепи. Содержит в себе всю необходимую информацию для отображения в ПДА.
А именно:
    заголовок
    тип
    фон
    сообщение и его тип
    отображаемый на экран текст (с условием)
    переходы (с названием, след. стадией  и условием отображения)
    действия которые необходимо выполнить при прохождении этой стадии

Пример стадии
```
{
      "id": 2,
      "type_stage": 0,
      "background_url": "story/prologue/screen/chapter_0_0.jpg",
      "title": "Как играть?",
      "message": "",
      "type_message": 0,
      "texts": [
        {
          "text": "Каждый сюжет состоит из двух элементов: текстовые действия и действия на карте. Текстовые действия будут происходить аналогично тем, которые ты видишь сейчас. В текстовых действиях тебе будет даваться на выбор несколько вариантов развития событий, дальнейшие события в игре будут зависеть от твоих ответов. События на карте ты увидешь позже.",
          "condition": {}
        },
        {
          "text": "",
          "condition": {}
        }
      ],
      "transfers": [
        {
          "text": "Далее",
          "stage_id": "3",
          "condition": {}
        }
      ],
      "actions": {
		"-":["relation_2:3","relation_5:5","relation_1:3","relation_3:3","relation_6:2"]
      }
}
```
Пустые поля, которые не используются можно опустить. В примере это "condition":{}, второй текст без условия, message и type_message 

Типы стадий:

0 - обычная сцена

1 - сцена разговора

4 - переход на карту

5 - загрузка другой главы

6 - переход на продавца

В тексте могут использоваться placeholders по типу
"@name"
"@nickname"
"@money"
"@xp"
"@login"
"@location"
"@group"
"@pdaId"

Загрузив самую первую стадию, приложение будет ждать выбора ответа
...
