<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Carro ${cab.plate.value} - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
</head>
<body class="device">
<div class="device-frame">
    <header class="device-header">
        <span class="brand">myCAB</span>
        <span class="plate">${cab.plate.value}</span>
        <span class="badge ${cab.status}">${cab.status}</span>
    </header>

    <section class="messages" id="messages">
        <h2>Mensagens</h2>
        <ul class="msg-list" id="msg-list">
            <c:forEach var="m" items="${recent}">
                <li><time>${m.createdAt}</time><p>${m.body}</p></li>
            </c:forEach>
        </ul>
    </section>

    <section class="location">
        <h2>Atualizar posicao</h2>
        <form method="post" action="<c:url value='/cab/${cab.id}/location'/>">
            <label>Latitude</label>
            <input type="text" name="latitude" value="${cab.location != null ? cab.location.latitude : ''}" required/>
            <label>Longitude</label>
            <input type="text" name="longitude" value="${cab.location != null ? cab.location.longitude : ''}" required/>
            <button class="btn primary" type="submit">Enviar posicao</button>
        </form>
    </section>
</div>

<script type="text/javascript">
(function() {
    var url = "<c:url value='/api/messages/CAB/${cab.id}'/>";
    function poll() {
        $.getJSON(url, function(data) {
            for (var i = 0; i < data.length; i++) {
                var li = $("<li/>");
                li.append($("<time/>").text(new Date(data[i].createdAt).toLocaleTimeString()));
                li.append($("<p/>").text(data[i].body));
                $("#msg-list").prepend(li);
            }
        });
    }
    setInterval(poll, 5000);
})();
</script>
</body>
</html>
