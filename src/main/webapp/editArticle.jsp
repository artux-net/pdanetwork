<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html>
<head>
    <meta charset="utf-8">
    <title>Админ панель - Сталкерский ПДА | Artux</title>
    <style type="text/css">

        <%@include file="/css/jodit.es2018.min.css" %>
        <%@include file="/css/style.css" %>

    </style>
    <script type="text/javascript" src="/pda/js/jodit.es2018.min.js"></script>
</head>
<body>
<div class="container">
    <div class="header"><img src="https://artux.net/favicon.ico" style="height: 50px"></div>
    <jsp:include page="/sidebar.jsp"/>
    <div class="content">
        <form name="addArticle" method="post" action="${pageContext.request.contextPath}/admin">
            <input type="text" name="title" placeholder="Название" value="${a.title}"/>
            <input type="text" name="pic" placeholder="Ссылка на картинку" value="${a.image}"/>
            <input type="text" name="desc" placeholder="Описание" value="${a.description}"/>
            <input type="text" name="tags" placeholder="Теги через запятую" value="${a.tags}"/>
            <input type="hidden" name="action" value="editArticle" />
            <input type="hidden" name="feedId" value="${a.feedId}" />
            <textarea name="content" id="content">${a.content}</textarea>
            <button type="submit">Изменить статью</button>
        </form>


        <script type="text/javascript">
            var editor = new Jodit('#content', {
                uploader: {
                    url: 'http://localhost/files'
                },
                "language": "en",
                "theme": "dark",
                "disablePlugins": "about"
            });
        </script>
    </div>
    <div class="footer">${footer_version}</div>
</div>
</body>
</html>