<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Восстановить пароль - Сталкерский ПДА</title>
    <style>
        input, option, select, textarea {
            background: #0c0c0c;
            color: #fff;
            padding: 0 5px;
            border: 1px solid #363636;
            width: 90%;
        }

        button {
            background: #212b35 !important;
            border: solid 1px #2d3c4a !important;
            border-top: none !important;
            color: #6d8194 !important;
        }

        body {
            background-color: #1a1b1d;
            color: #fff;
            font-size: 14px;
            display: inline-block;
        }
    </style>
</head>
<body>
Вы отправили запрос на смену пароля в Сталкерском ПДА. Пожалуйста, введите новый пароль в поле ниже. <br><br>
<form storyId="pass" action="${pageContext.request.contextPath}/reset" method="post">
    <input type="password" name="password" storyId="password" value="${password}" placeholder="Введите новый пароль">
</form>
<center>
    <button type="submit" form="pass">Отправить</button>
</center>
</body>
</html>