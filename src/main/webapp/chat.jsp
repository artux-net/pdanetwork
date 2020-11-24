<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Админ панель - Сталкерский ПДА | Artux</title>
    <style>
        <%@include file="/css/style.css" %>
    </style>

    <script type="text/javascript">

        var socket;

        function connect() {
            socket = new WebSocket("wss://pda.artux.net/chat/${m.token}");

            socket.onopen = function () {
                console.log('session opened.');
                var window = document.getElementById("messages");
                window.innerHTML = '';
            };

            socket.onclose = function (event) {
                setTimeout(function () {
                    console.log('reconnect.');
                    connect();
                }, 1000);
            };

            socket.onmessage = function (event) {
                addRow(event.data);
            };

            socket.onerror = function (error) {
                alert("Ошибка " + error.message);
                socket.close();
            };

            function addRow(data) {
                var message = JSON.parse(data);

                var spn = document.createElement('span');
                spn.innerHTML = "<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%\">\n" +
                    "            <tbody>\n" +
                    "            <tr>\n" +
                    "                <td>" + message.senderLogin + " PDAID #" + message.pdaId + "</td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td>\n" +
                    "                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%\">\n" +
                    "                        <tbody>\n" +
                    "                        <tr>\n" +
                    "                            <td>" + message.message + "</td>\n" +
                    "                            <td>" + message.time + "</td>\n" +
                    "                        </tr>\n" +
                    "                        </tbody>\n" +
                    "                    </table>\n" +
                    "                    <p align=\"center\"><a href=\"{chat_delete_message}\">Удалить</a> | <a href=\"chat_ban_1hours\">Бан на\n" +
                    "                        час</a></p>\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "            </tbody>\n" +
                    "        </table>\n" +
                    "        <hr>";
                document.getElementById('messages').appendChild(spn);
                var window = document.getElementById("messages");
                window.scrollTop = window.scrollHeight;
                if (window.childElementCount > 50) {
                    window.removeChild(window.firstChild);
                }
            }
        }

        connect();

        function sendMessage() {
            socket.send(document.getElementById("input_text").value);
        }

        function helpChat() {
            alert("\n" +
                "`0` - Одиночки\n" +
                "\n" +
                "`1` - Бандиты\n" +
                "\n" +
                "`2` - Военные\n" +
                "\n" +
                "`3` - Свобода\n" +
                "\n" +
                "`4` - Долг\n" +
                "\n" +
                "`5` - Монолит\n" +
                "\n" +
                "`6` - Наёмники");
        }

    </script>

</head>
<body>
<div class="container">
    <div class="header"><img src="https://artux.net/favicon.ico" style="height: 50px"></div>
    <div class="sidebar">
        <p>${username}</p>
        <p><a href="${link_back}">Назад</a></p>
    </div>
    <div class="content">
        <h2>Чат</h2>

        <div id="messages" style="height: 400px; overflow: auto;">
            <!--Начало сообщения-->
            <!--Конец сообщения-->
        </div>
        <p>Сообщение<Br>
            <textarea name="comment" cols="40" rows="3" id="input_text"></textarea></p>
        <p><input type="submit" value="Отправить" onclick="sendMessage()">
            <input type="reset" value="Очистить">
            <input type="reset" value="Переподключиться" onclick="connect()">
        </p>
    </div>

    <div class="footer">${footer_version}</div>
</div>
</body>
</html>