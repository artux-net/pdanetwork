# PDA Network
PDA Network with API for application "Сталкерский ПДА"
## Getting Started

### Registration

Do POST request to
`/api/register` or `/register`

```
{
  "login": "PrisonerOftheWorld",
  "name": "Maksim Prygunov",
  "email": "maksim.prygunov@mail.com",
  "password":"itsnotpassword",
  "groupId":"4"
}
``` 
Response looks like this

```
{
    "success": true,
    "exists": false,
    "code": 200,
    "description": "OK"
}
```
Bad request

```
{
    "success": false,
    "exists": true,
    "code": 400,
    "description": "Wrong email"
}
```


### Login
Do POST request to
`/api/login` or `/login`

```
{
   "emailOrLogin": "PrisonerOftheWorld",
   "password":"itsnotpassword"
}
```

Response looks like this

```
{
    "success": true,
    "code": 200,
    "token": "W0Mk8Xoz3TJNU+LY"
}
```

Bad request
```
{
    "success": false,
    "code": 401,
    "token": "null"
}
```

### Chat

Via WebSocket

Address: ``ws://localhost:8080/chat``

Sending message 

```
{
     "senderLogin":"PrisonerOftheWorld",
     "message":"Hello everybody",
     "time":"sometime"

}
```

## Other information

### Groups

`0` - Одиночки

`1` - Бандиты

`2` - Военные

`3` - Свобода

`4` - Долг

`5` - Монолит

`6` - Наёмники

### Error Codes

`0` - OK

`1` - Wrong Login or Email

`2` - Wrong password

`3` - Wrong token

`4` - Server error

###Rangs
0-500 = отмычка 

500-1500 = новичок

1500-3500 = сталкер

3500-6000 = опытный

6000-9000 = ветеран

9000-12000 = мастер

12000-22000 = легенда
