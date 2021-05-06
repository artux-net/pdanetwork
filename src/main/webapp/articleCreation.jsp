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
        <form name="addArticle" method="post" action="${pageContext.request.contextPath}/admin?action=addArticle">
            <input type="text" name="title" placeholder="Название" value="${title}"/>
            <input type="text" name="pic" placeholder="Ссылка на картинку" value="${pic}"/>
            <input type="text" name="desc" placeholder="Описание" value="${desc}"/>
            <input type="text" name="tags" placeholder="Теги через запятую" value="${tags}"/>
            <textarea name="editor" id="editor">${content}</textarea>
            <button type="submit">Добавить статью</button>
        </form>
        <c:forEach items="${articles}" var="a" varStatus="item">
            <div>
                <hr>
                <form name="addArticle" method="post"
                      action="${pageContext.request.contextPath}/admin?action=removeArticle">
                    <input type="hidden" name="feedId" value="${a.feedId}" />
                    <button type="submit">Удалить статью</button>
                </form>
                <form name="editArticle" method="get"
                      action="${pageContext.request.contextPath}/admin">
                    <input type="hidden" name="action" value="editArticle" />
                    <input type="hidden" name="feedId" value="${a.feedId}" />
                    <button type="submit">Изменить статью</button>
                </form>
                <h1>${a.title}</h1>
                <p>${a.description}</p>
                <p>${a.tags}</p>
                <p>${a.image}</p>
                <p>${a.published}</p>
                <div>${a.content}</div>
                <hr>
            </div>
        </c:forEach>

        <script type="text/javascript">
            var editor = new Jodit('#editor', {
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