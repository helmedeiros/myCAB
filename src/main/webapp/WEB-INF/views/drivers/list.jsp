<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Motoristas - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar">
    <a class="brand" href="<c:url value='/'/>">myCAB</a>
    <ul class="nav">
        <li><a href="<c:url value='/operator'/>">Central</a></li>
        <li><a href="<c:url value='/cabs'/>">Frota</a></li>
        <li><a href="<c:url value='/review'/>">Aprovacoes</a></li>
        <li class="active"><a href="<c:url value='/drivers'/>">Motoristas</a></li>
        <li><a href="<c:url value='/customers'/>">Clientes</a></li>
    </ul>
</div>
<div class="container">
    <h1>Motoristas</h1>
    <form method="get" action="<c:url value='/drivers'/>" class="search">
        <select name="category">
            <option value="">Todas categorias</option>
            <c:forEach var="cat" items="${categories}">
                <option value="${cat}" ${cat == selected ? 'selected' : ''}>${cat}</option>
            </c:forEach>
        </select>
        <button class="btn" type="submit">Filtrar</button>
    </form>
    <table class="table">
        <thead><tr><th>Nome</th><th>E-mail</th><th>Telefone</th><th>Categoria</th></tr></thead>
        <tbody>
        <c:forEach var="d" items="${drivers}">
            <tr><td>${d.fullName}</td><td>${d.email}</td><td>${d.phone}</td><td>${d.preferredCategory}</td></tr>
        </c:forEach>
        <c:if test="${empty drivers}"><tr><td colspan="4">Nenhum motorista.</td></tr></c:if>
        </tbody>
    </table>
</div>
</body>
</html>
