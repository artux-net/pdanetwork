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
    <div class="header">Artux</div>
    <div class="sidebar">
        <b><p><a href="https://pda.artux.net/">Я не туда попал..</a></p></b>
    </div>
    <div class="content">
        <h2>Вход в панель</h2>
        <div class="outer">
            <div class="middle">
                <div class="inner">
                    <div class="login-wr">
                        <form name="login" method="post" action="${pageContext.request.contextPath}/loginAdmin">
                            <div class="form">
                                <input type="text" name="login" placeholder="Пользователь" value="${login}"/>
                                <input type="password" name="password" placeholder="Пароль" value="${password}"/>
                                <button> Авторизация</button>
                            </div>
                        </form>
                        <p>${violation}</p>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <div class="footer">${footer_version}</div>

</div>
</body>
</html>