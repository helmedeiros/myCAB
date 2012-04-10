<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Cadastrar carro - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar">
    <a class="brand" href="<c:url value='/'/>">myCAB</a>
</div>
<div class="container narrow">
    <h1>Cadastrar carro</h1>
    <form method="post" action="<c:url value='/cabs'/>">
        <label>Placa</label>
        <input type="text" name="plate" placeholder="ABC-1234" required/>

        <label>Modelo</label>
        <select name="modelId" required>
            <c:forEach var="m" items="${models}">
                <option value="${m.id}">${m.displayName} (${m.category})</option>
            </c:forEach>
        </select>

        <p>
            <button class="btn primary" type="submit">Cadastrar</button>
            <a class="btn" href="<c:url value='/cabs'/>">Cancelar</a>
        </p>
    </form>
</div>
</body>
</html>
