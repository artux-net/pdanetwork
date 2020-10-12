<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
</head>
<body>
<div style="text-align: center;">
<h1>Войдите</h1>
<form action="${pageContext.request.contextPath}/loginAdmin" method="post">
    <label for="login">Email or login: </label>
    <input type="text" name="login" value="${login}">
    <p><label for="password">Password: </label>
        <input type="password" name="password" value="${password}">
    </p>
    <input type="submit" name="sign in" value="Sign in">
</form>
    <p>${violation}</p>
</div>

</body>
</html>