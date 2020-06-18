var socket = new WebSocket("ws://35.204.191.66:8080/chat?t=9a4a8b475020a4c6fe2b0027d3c151dfd3a54d24a54ce025bceb840eed1c83617d0216d6d597c904927dd9dc0575979e");

socket.onopen = function() {
    alert('open');
};

socket.onclose = function(event) {
    alert('Код: ' + event.code + ' причина: ' + event.reason);
};

socket.onmessage = function(event) {
    addRow(event.data);
};

socket.onerror = function(error) {
    alert("Ошибка " + error.message);
};

function addRow(data) {

    var message = JSON.parse(data);
    var table = document.getElementById("dataTable");

    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);

    if(rowCount>50){
        table.deleteRow(0);
    }

    var cell1 = row.insertCell(0);
    cell1.innerHTML = message.senderLogin + " PDAID #" + message.pdaId;

    var cell2 = row.insertCell(1);
    cell2.innerHTML = "<textarea cols=\"50\" readonly>" + message.message + "</textarea>";

    var objDiv = document.getElementById("divChat");
    objDiv.scrollTop = objDiv.scrollHeight;
}

function sendMessage() {
    var message = { senderLogin: document.getElementById("login").value, message: document.getElementById("input_text").value, time: new Date(), groupId: document.getElementById("groupId").value, avatarId: document.getElementById("avatarId").value, pdaId: document.getElementById("pdaId").value };
    socket.send(JSON.stringify(message))
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