<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Админ-панель</title>
    <style>
        .dropdown {
            position: relative;
            display: inline-block;
        }

        .dropdown-content {
            display: none;
            position: absolute;
            background-color: #f9f9f9;
            min-width: 160px;
            min-height: 300px;
            box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
            padding: 12px 16px;
            z-index: 1;
        }

        .dropdown:hover .dropdown-content {
            display: block;
        }
    </style>
</head>
<body>
<p>${loginedAs}</p>

<div style="text-align: right;">
<script type="text/javascript" src="websocket.js"></script>



<div style="height: 400px;  width:530px; float: right;">

    <div class="dropdown" style="float: left;">
        <span>Settings</span>
        <div class="dropdown-content">
            <label for="login">Login: </label>
            <input name="login" type="text" id="login" value="Система">

            <label for="pdaId">PdaID: </label>
            <input name="pdaId" type="number" id="pdaId" value="0">

            <label for="groupId">GroupID: </label>
            <input name="groupId" type="number" id="groupId" value="0">

            <label for="avatarId">AvatarID: </label>
            <input name="avatarId" type="number" id="avatarId" value="30">

            <label for="fromMember">From me:</label>
            <input name="avatarId" type="checkbox" id="fromMember">


            <input type="submit" name="send" value="Help" onclick="helpChat()"/>
        </div>
    </div>


    <div id="divChat" style="height: 400px;  width:530px; overflow: auto;">
    <TABLE id="dataTable" width="500px" border="1">
    </TABLE>
    </div>

    <input name="message" type="text" id="input_text"/>
    <input type="submit" name="send" value="Send" style="max-width: 350px;" onclick="sendMessage()"/>
</div>


</div>

</body>
</html>