<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Админ панель - Сталкерский ПДА | Artux</title>
    <style>
        <%@include file="/css/style.css" %>
    </style>
</head>
<body>
<div class="container">
    <div class="header"><img src="https://artux.net/favicon.ico" style="height: 50px"></div>
    <div class="sidebar">
        <p><a href="#">${username}</a></p>
        <p><a href="${link_back}">Назад</a></p>
    </div>
    <div class="content">
        <h2>Чат</h2>
        <!--Начало сообщения-->
        <table border="0" cellpadding="1" cellspacing="1" style="width: 100%">
            <tbody>
            <tr>
                <td>{chat_nickname}</td>
            </tr>
            <tr>
                <td>
                    <table border="0" cellpadding="0" cellspacing="0" style="width: 100%">
                        <tbody>
                        <tr>
                            <td>{chat_message}</td>
                            <td>{chat_time_message}</td>
                        </tr>
                        </tbody>
                    </table>
                    <p align="center"><a href="{chat_delete_message}">Удалить</a> | <a href="chat_ban_1hours">Бан на
                        час</a></p>
                </td>
            </tr>
            </tbody>
        </table>
        <hr>
        <!--Конец сообщения-->
        <form name="chat-post" method="post" action="">

            <p>Сообщение<Br>
                <textarea name="comment" cols="40" rows="3"></textarea></p>
            <p><input type="submit" value="Отправить">
                <input type="reset" value="Очистить"></p>
        </form>

    </div>
    <div class="footer">${footer_version}</div>
</div>
</body>
</html>