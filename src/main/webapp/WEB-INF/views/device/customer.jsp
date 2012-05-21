<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Cliente ${customer.name} - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
    <script type="text/javascript" src="<c:url value='/static/js/jquery-1.7.2.min.js'/>"></script>
</head>
<body class="device">
<div class="device-frame">
    <header class="device-header">
        <span class="brand">myCAB</span>
        <span class="customer">${customer.name}</span>
    </header>
    <section class="messages" id="messages">
        <h2>Avisos da central</h2>
        <ul class="msg-list" id="msg-list">
            <c:forEach var="m" items="${recent}">
                <li><time>${m.createdAt}</time><p>${m.body}</p></li>
            </c:forEach>
            <c:if test="${empty recent}">
                <li class="empty">Nenhuma mensagem ainda.</li>
            </c:if>
        </ul>
    </section>
</div>

<script type="text/javascript">
(function() {
    var url = "<c:url value='/api/messages/CUSTOMER/${customer.id}'/>";
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
