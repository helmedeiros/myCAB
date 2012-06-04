<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Modelos - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar">
    <a class="brand" href="<c:url value='/'/>">myCAB</a>
    <ul class="nav">
        <li><a href="<c:url value='/operator'/>">Central</a></li>
        <li><a href="<c:url value='/cabs'/>">Frota</a></li>
        <li><a href="<c:url value='/customers'/>">Clientes</a></li>
        <li class="active"><a href="<c:url value='/models'/>">Modelos</a></li>
    </ul>
</div>
<div class="container">
    <h1>Modelos disponiveis</h1>
    <table class="table">
        <thead><tr><th>Fabricante</th><th>Modelo</th><th>Categoria</th></tr></thead>
        <tbody>
        <c:forEach var="m" items="${models}">
            <tr><td>${m.make}</td><td>${m.model}</td><td>${m.category}</td></tr>
        </c:forEach>
        </tbody>
    </table>
    <h2>Adicionar modelo</h2>
    <form method="post" action="<c:url value='/models'/>">
        <label>Fabricante</label>
        <input type="text" name="make" required/>
        <label>Modelo</label>
        <input type="text" name="model" required/>
        <label>Categoria</label>
        <select name="category" required>
            <c:forEach var="cat" items="${categories}">
                <option value="${cat}">${cat}</option>
            </c:forEach>
        </select>
        <p><button class="btn primary" type="submit">Adicionar</button></p>
    </form>
</div>
</body>
</html>
