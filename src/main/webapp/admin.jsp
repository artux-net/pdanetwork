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
    <jsp:include page="/sidebar.jsp"/>
    <div class="content">
        <h2>Статистика</h2>
        <table border="0" cellpadding="1" cellspacing="1" style="width: 100%">
            <tbody>
            <tr>
                <td><b>Пользователей</b></td>
                <td>${total_registrations}</td>
                <td><b>Регистраций за сутки</b></td>
                <td>${registrations}</td>
            </tr>
            <tr>
                <td><b>Время на сервере</b></td>
                <td>${server_time}</td>
                <td><b>Денег в обороте</b></td>
                <td>{total_money}</td>
            </tr>
            <tr>
                <td><b>Онлайн за сутки</b></td>
                <td>${online}</td>
                <td><b>Средний онлайн за сутки</b></td>
                <td>{average_online} ч.</td>
            </tr>
            </tbody>
        </table>
        <hr>
        <h2>Топ пользователей</h2>
        <table border="0" cellpadding="1" cellspacing="1" style="width: 100%">
            <tbody>
            <tr>
                <td><b>№</b></td>
                <td><b>#ПДА</b></td>
                <td><b>Ник</b></td>
                <td><b>Часов</b></td>
                <td><b>Опыт</b></td>
                <td><b>Денег</b></td>
            </tr>
            <c:forEach items="${rating}" var="user" varStatus="item">
                <tr>
                    <td><b>${item.index+1}</b></td>
                    <td>${user.pdaId}</td>
                    <td>${user.login}</td>
                    <td>${user.registrationDate}</td>
                    <td>${user.xp}</td>
                    <td>${user.money}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <p>&nbsp;</p>

    </div>
    <div class="footer">${footer_version}</div>
</div>
</body>
</html>