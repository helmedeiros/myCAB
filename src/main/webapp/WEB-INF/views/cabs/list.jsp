<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Frota - myCAB</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/static/css/mycab.css'/>"/>
</head>
<body>
<div class="navbar">
    <a class="brand" href="<c:url value='/'/>">myCAB</a>
    <ul class="nav">
        <li><a href="<c:url value='/operator'/>">Central</a></li>
        <li class="active"><a href="<c:url value='/cabs'/>">Frota</a></li>
        <li><a href="<c:url value='/customers'/>">Clientes</a></li>
    </ul>
</div>
<div class="container">
    <h1>Frota</h1>
<p>Cadastre e acompanhe os carros disponiveis na central.</p>
    <p><a class="btn primary" href="<c:url value='/cabs/new'/>">Cadastrar carro</a></p>
    <table class="table">
        <thead>
        <tr>
            <th>Placa</th>
            <th>Modelo</th>
            <th>Categoria</th>
            <th>Status</th>
            <th>Posicao</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="cab" items="${cabs}">
            <tr>
                <td>${cab.plate.value}</td>
                <td>${cab.model.displayName}</td>
                <td>${cab.model.category}</td>
                <td><span class="badge ${cab.status}">${cab.status}</span></td>
                <td>
                    <c:if test="${cab.location != null}">
                        ${cab.location.latitude}, ${cab.location.longitude}
                    </c:if>
                </td>
                <td>
                    <form method="post" action="<c:url value='/cabs/${cab.id}/delete'/>" style="display:inline">
                        <button class="btn">Remover</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty cabs}">
            <tr><td colspan="6">Nenhum carro cadastrado.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>
