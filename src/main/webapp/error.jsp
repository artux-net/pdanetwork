<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>PDA Error</title>

    <style media>
        body {
            background: #232121;
            color: #fff;
        }
        P.fig {
            text-align: center;
        }
        @font-face {
            font-family: Tahoma;
        }

        P {
            font-family: Tahoma;
            font-size: 20px;
        }
        img {
            width: 100%;
            height: auto;
        }
        .rightpic {
            float: right;
            margin: 0;
        }
    </style>
</head>

<body>
<p class="fig">
    <img src="https://github.com/ridiculien/site/blob/gh-pages/logo.jpg?raw=true"/>
</p>

<p align="center">Не туда забрел, приятель...</p>
<p align="center">Код: ${code}, ${message}</p>

<p class="rightpic">
    <img src="https://github.com/ridiculien/site/blob/gh-pages/%D0%91%D0%B5%D0%B7-%D0%B8%D0%BC%D0%B5%D0%BD%D0%B8-1.gif?raw=true"/>
</p>
</body>
</html>