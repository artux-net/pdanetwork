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
            socket = new WebSocket("wss://api.artux.net/pda/chat/${m.token}");

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
                if ("time" in message) {
                    addMessage(message);
                } else {
                    document.getElementById('messages').innerHTML = '';
                    for (var i = 0; i < message.length; i++) {
                        addMessage(message[i]);
                    }
                }

                var window = document.getElementById("messages");
                window.scrollTop = window.scrollHeight;
                if (window.childElementCount > 50) {
                    window.removeChild(window.firstChild);
                }
            }
        }

        function addMessage(message) {
            var spn = document.createElement('span');
            spn.innerHTML =
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
                "                            <td>" + new Date(message.time).toString() + "</td>\n" +
                "                        </tr>\n" +
                "                        </tbody>\n" +
                "                    </table>\n" +
                "                    <p align=\"center\">" +
                "" + "<form method=\"POST\" action=\"${pageContext.request.contextPath}/admin/chat?action=remove&time=" + message.time + "\"><button type=\"submit\">Удалить</button></form>" +
                "" + "<form method=\"POST\" action=\"${pageContext.request.contextPath}/admin/chat?action=ban&pdaId=" + message.pdaId + "\"><button type=\"submit\">Бан на час</button></form>"
                "                </td>\n" +
                "            </tr>\n" +
                "            </tbody>\n" +
                "        <hr>";
            document.getElementById('messages').appendChild(spn);
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
    <jsp:include page="/sidebar.jsp"/>
    <div class="content">
        <h2>Чат</h2>

        <div style="height: 400px; overflow: auto;">

            <table id="messages" border="0" cellpadding="1" cellspacing="1" style="width: 100%">

            </table>
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